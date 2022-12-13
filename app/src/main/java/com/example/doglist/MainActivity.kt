package com.example.doglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.NetworkImageView
import com.example.doglist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var model : DogViewModel
    private val dogAdapter = DogAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.list.apply{
            adapter=dogAdapter
            layoutManager= LinearLayoutManager(applicationContext)
            itemAnimator= DefaultItemAnimator()
            setHasFixedSize(true)
        }

        model = ViewModelProvider(this)[DogViewModel::class.java]
        model.list.observe(this){
            dogAdapter.notifyItemRangeInserted(0, dogAdapter.itemCount)
        }

        model.requestDog()
    }

    inner class DogAdapter: RecyclerView.Adapter<DogAdapter.ViewHolder>(){
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            val txBreed = itemView.findViewById<TextView>(R.id.breed)
            val niImage: NetworkImageView = itemView.findViewById<NetworkImageView>(R.id.image)

            init{
                niImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
                itemView.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                val intent = Intent(application, DogActivity::class.java)
                val dog = model.list.value?.get(adapterPosition)
                intent.putExtra(DogActivity.KEY_BREED, dog?.breed)
                intent.putExtra(DogActivity.KEY_SIZE, dog?.size)
                intent.putExtra(DogActivity.KEY_COAT, dog?.coat)
                intent.putExtra(DogActivity.KEY_IMAGE, model.getImageUrl(adapterPosition))
                startActivity(intent)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(R.layout.item_dog, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.txBreed.text=model.list.value?.get(position)?.breed
            holder.niImage.setImageUrl(model.getImageUrl(position), model.imageLoader)
        }

        override fun getItemCount(): Int {
            return model.list.value?.size?:0;
        }
    }
}