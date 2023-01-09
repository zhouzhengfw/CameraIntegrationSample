package com.firework.sdkdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.firework.sdkdemo.databinding.FragmentFirstBinding
import com.loopnow.library.auth.R
import com.loopnow.library.auth.activity.LoginWebViewActivity
import com.loopnow.library.auth.api.Api
import com.loopnow.library.auth.util.FWEventName
import com.loopnow.library.auth.util.Helper
import com.loopnow.library.auth.util.ProviderUtil
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
private const val REQUEST_RESULT = 1001

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            val intent = Intent(requireActivity(), LoginWebViewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            requireActivity().startActivityForResult(intent, 0)
            requireActivity().registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback {
                    it?.apply {

                        if (this.resultCode == REQUEST_RESULT) {
                            if (resultCode == Activity.RESULT_OK) {
                                if (data != null) {
                                    val credential = Api.credential
                                    if (credential != null && !credential.accessToken.isNullOrBlank()) {
                                        lifecycleScope.launch {
                                            try {
                                                Api.syncCurrentOrganization()
                                                ProviderUtil.trackVisitorEvents(FWEventName.SESSION_LOGGED_IN)
//                                                launchHome()
                                            } catch (e: Exception) {
                                                Helper.showToast(requireActivity(), requireActivity().getString(R.string.login_failed))
                                            }
                                        }
                                    }
                                }
                                Helper.showToast(requireActivity(), requireActivity().getString(R.string.login_failed))
                            }
                        }

                    }
                }).launch(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}