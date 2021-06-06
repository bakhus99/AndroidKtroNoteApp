package com.bakhus.noteapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bakhus.noteapp.R
import com.bakhus.noteapp.data.local.entites.Note
import com.bakhus.noteapp.databinding.ItemNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private var onItemClickListener: ((Note) -> Unit)? = null

    private val differ = AsyncListDiffer(this, diffCallback)

    var notes: List<Note>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {

        val binding =
            ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)

    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]

        val tvNoteTitle = holder.itemView.findViewById<TextView>(R.id.tvTitle)
        val ivIsSync = holder.itemView.findViewById<ImageView>(R.id.ivSynced)
        val tvIsSync = holder.itemView.findViewById<TextView>(R.id.tvSynced)
        val tvDate = holder.itemView.findViewById<TextView>(R.id.tvDate)
        val viewNoteColor = holder.itemView.findViewById<View>(R.id.viewNoteColor)
        holder.itemView.apply {

            tvNoteTitle.text = note.title
            if (!note.isSync) {
                ivIsSync.setImageResource(R.drawable.ic_cross)
                tvIsSync.text = context.getString(R.string.not_synced)
            } else {
                ivIsSync.setImageResource(R.drawable.ic_check)
                tvIsSync.text = context.getString(R.string.synced)
                    //"Synced"
            }

            val dateFormat = SimpleDateFormat("dd.MM.yy, HH:mm", Locale.getDefault())
            val dateString = dateFormat.format(note.date)
            tvDate.text = dateString

            val drawable = ResourcesCompat.getDrawable(resources, R.drawable.circle_shape, null)
            drawable?.let {
                val wrapperDrawable = DrawableCompat.wrap(it)
                val color = Color.parseColor("#${note.color}")
                DrawableCompat.setTint(wrapperDrawable, color)
                viewNoteColor.background = wrapperDrawable
            }
            setOnClickListener {
                onItemClickListener?.let { click ->
                    click(note)
                }
            }
        }
    }

    override fun getItemCount() = notes.size


    fun setOnItemClickListener(onItemClick: (Note) -> Unit) {
        this.onItemClickListener = onItemClick
    }

}