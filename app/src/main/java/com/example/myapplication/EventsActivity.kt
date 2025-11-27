class EventsAdapter(
    private val events: MutableList<Event>,
    private val onClick: (position: Int) -> Unit
) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgEvent: ImageView = itemView.findViewById(R.id.imgEvent)
        val tvDateTime: TextView = itemView.findViewById(R.id.tvDateTime)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvInstructor: TextView = itemView.findViewById(R.id.tvInstructor)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_item, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.tvDateTime.text = event.dateTime
        holder.tvTitle.text = event.title
        holder.tvInstructor.text = event.instructor

        // Status
        when(event.status) {
            "DISPONÍVEL" -> {
                holder.tvStatus.text = "DISPONÍVEL"
                holder.tvStatus.setBackgroundResource(R.drawable.status_available_bg)
                holder.tvStatus.setTextColor(Color.parseColor("#28A745"))
            }
            "CONCLUÍDO" -> {
                holder.tvStatus.text = "CONCLUÍDO"
                holder.tvStatus.setBackgroundResource(R.drawable.status_done_bg)
                holder.tvStatus.setTextColor(Color.GRAY)
            }
            "AGENDADO" -> {
                holder.tvStatus.text = "AGENDADO"
                holder.tvStatus.setBackgroundResource(R.drawable.status_available_bg)
                holder.tvStatus.setTextColor(Color.parseColor("#007BFF"))
            }
        }

        holder.itemView.setOnClickListener {
            if(event.status == "DISPONÍVEL") {
                event.status = "AGENDADO"
                notifyItemChanged(position)
            }
            onClick(position)
        }
    }

    override fun getItemCount(): Int = events.size
}
