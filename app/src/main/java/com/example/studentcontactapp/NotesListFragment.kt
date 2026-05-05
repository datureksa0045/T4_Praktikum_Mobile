package com.example.studentcontactapp

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import utils.FileHelper
import java.io.File

class NotesListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_notes_list, container, false)
        val rvNotes = view.findViewById<RecyclerView>(R.id.rvNotes)
        
        val notesList = FileHelper.getAllNotes(requireContext())
        
        rvNotes.layoutManager = LinearLayoutManager(context)
        rvNotes.adapter = NotesAdapter(notesList)
        
        return view
    }

    class NotesAdapter(private val files: List<File>) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val tvName: TextView = v.findViewById(android.R.id.text1)
            val tvSize: TextView = v.findViewById(android.R.id.text2)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val file = files[position]
            holder.tvName.text = file.name
            holder.tvSize.text = "${file.length()} bytes"
        }

        override fun getItemCount() = files.size
    }
}
