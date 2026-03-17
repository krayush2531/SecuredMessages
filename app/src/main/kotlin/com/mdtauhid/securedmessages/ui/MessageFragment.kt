package com.mdtauhid.securedmessages.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdtauhid.securedmessages.database.DatabaseProvider
import com.mdtauhid.securedmessages.databinding.FragmentMessagesBinding
import com.mdtauhid.securedmessages.parser.SmsCategory
import com.mdtauhid.securedmessages.repository.MessageRepository
import com.mdtauhid.securedmessages.viewmodel.MessageViewModel
import com.mdtauhid.securedmessages.viewmodel.MessageViewModelFactory

class MessageFragment : Fragment() {

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: SmsCategory?): MessageFragment {
            return MessageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY, category?.name)
                }
            }
        }
    }

    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MessageViewModel by activityViewModels {
        MessageViewModelFactory(
            MessageRepository(
                DatabaseProvider.getDatabase(requireContext()).messageDao()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MessageAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        val categoryName = arguments?.getString(ARG_CATEGORY)
        val category = categoryName?.let { SmsCategory.valueOf(it) }

        val liveData = if (category == null) {
            viewModel.allMessages
        } else {
            viewModel.getMessagesByCategory(category)
        }

        liveData.observe(viewLifecycleOwner) { messages ->
            adapter.submitList(messages)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
