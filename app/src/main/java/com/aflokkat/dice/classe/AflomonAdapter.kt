package com.aflokkat.dice.classe

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.aflokkat.dice.R
import com.aflokkat.dice.activite.CombatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AflomonAdapter(private val dataSet: Array<Aflomon>) :
    RecyclerView.Adapter<AflomonAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val imageView : ImageView
        val constraintLayout: ConstraintLayout
        val checkBox : CheckBox

        init {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.textView2)
            imageView = view.findViewById(R.id.imageView)
            constraintLayout = view.findViewById(R.id.cl_row)
            checkBox = view.findViewById(R.id.checkBox)

            constraintLayout.setOnClickListener {
                //Démarrer l'activité CombatActivity

            }

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_aflomon, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val aflomon = dataSet[position]
        viewHolder.textView.text = aflomon.nom
        viewHolder.imageView.setImageDrawable(ContextCompat.getDrawable(viewHolder.textView.context, aflomon.idImage))


        viewHolder.itemView.setOnClickListener {
            val intent = Intent(viewHolder.itemView.context, CombatActivity::class.java)
            intent.putExtra("aflomon", aflomon)
            viewHolder.itemView.context.startActivity(intent)
        }
        val context = viewHolder.itemView.context.applicationContext
        GlobalScope.launch {
            val db = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "aflodb"
            ).fallbackToDestructiveMigration().build()
            val aflomonDao = db.aflomonDao()
            val checked = aflomonDao.getAll()
            checked.forEach {
                if (it.nom == aflomon.nom) {
                    Dispatchers.Main.dispatch(coroutineContext,{
                        viewHolder.checkBox.isChecked = true
                    })

                }
            }
        }

        viewHolder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            // Si la case est cochée : On ajoute l'aflomon en BDD dans room
            // Si la case est decochée on retire l'aflomon en BDD dans room
            GlobalScope.launch {
                val db = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, "aflodb"
                ).fallbackToDestructiveMigration().build()
                val aflomonDao = db.aflomonDao()
                if (isChecked) {
                    aflomonDao.insertAll(aflomon)
                    println(aflomonDao.getAll())
                } else {
                    aflomonDao.delete(aflomon)
                    println(aflomonDao.getAll())
                }
            }




    }}

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
