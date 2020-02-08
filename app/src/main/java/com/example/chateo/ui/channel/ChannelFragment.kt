package com.example.chateo.ui.channel

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chateo.R
import com.example.chateo.databinding.FragmentChannelBinding
import com.getstream.sdk.chat.StreamChat
import com.getstream.sdk.chat.rest.User
import com.getstream.sdk.chat.view.MessageListView
import com.getstream.sdk.chat.viewmodel.ChannelViewModel
import com.getstream.sdk.chat.viewmodel.ChannelViewModelFactory

class ChannelFragment : Fragment() {
    private val args: ChannelFragmentArgs by navArgs()
   private lateinit var binding: FragmentChannelBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_channel,
            container,
            false
        )

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_channel, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().navigateUp()
            return true
        }
        return false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity: AppCompatActivity = activity as AppCompatActivity

        //toolbar setup
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar!!.setDisplayShowHomeEnabled(true)
        activity.supportActionBar!!.setDisplayShowTitleEnabled(true)


        val client = StreamChat.getInstance(activity.application)
        val view = view
        binding.lifecycleOwner = viewLifecycleOwner


        val channel = client.channel(args.channelType, args.channelId)
        val factory = ChannelViewModelFactory(activity.application, channel)
        val viewModel: ChannelViewModel by viewModels { factory }

        //connect the view model
        binding.viewModel = viewModel
        binding.messageList.setViewModel(viewModel, this)
        binding.messageInputView.setViewModel(viewModel, this)

        val messageList:MessageListView = view!!.findViewById(R.id.messageList)

        val otherUsers: List<User> = channel.channelState.otherUsers
        binding.avatarGroup.setChannelAndLastActiveUsers(channel, otherUsers, messageList.style)
        binding.channelName.text = channel.name
    }

}