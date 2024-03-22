package com.example.projectojulianortega

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException


class MainActivityListaDeMonedas : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var requestQueue: RequestQueue
    private lateinit var adapter: CoinAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_lista_de_monedas)

        listView = findViewById(R.id.listView)
        adapter = CoinAdapter()
        listView.adapter = adapter

        requestQueue = Volley.newRequestQueue(this)
        fetchData()
    }


    private fun fetchData() {
        val url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=eur&order=market_cap_desc&per_page=100&page=1&sparkline=false&locale=en"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    for (i in 0 until response.length()) {
                        val coin = response.getJSONObject(i)
                        val id = coin.getString("id")
                        val symbol = coin.getString("symbol")
                        val name = coin.getString("name")
                        val imageUrl = coin.getString("image")
                        val currentPrice = coin.getDouble("current_price")
                        val marketCap = coin.getDouble("market_cap")

                        val coinItem = CoinItem(id, symbol, name, imageUrl, currentPrice, marketCap)
                        adapter.addItem(coinItem)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            }
        )

        requestQueue.add(jsonArrayRequest)


    }

    private inner class CoinAdapter: BaseAdapter(){

        private val coinList = mutableListOf<CoinItem>()

        fun addItem(item: CoinItem) {
            coinList.add(item)
            notifyDataSetChanged()
        }

        override fun getCount(): Int {
            return coinList.size
        }

        override fun getItem (position: Int): Any {
            return coinList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var view = convertView
            val holder: ViewHolder

            if (view == null) {
                view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item, parent, false)
                holder = ViewHolder(view)
                view.tag = holder
            } else {
                holder = view.tag as ViewHolder
            }



            val currentItem = getItem(position) as CoinItem
            holder.textViewId.text = "ID: ${currentItem.id}"
            holder.textViewSymbol.text = "Symbol: ${currentItem.symbol}"
            holder.textViewName.text = "Name: ${currentItem.name}"
            holder.textViewPrice.text = "Price: ${currentItem.currentPrice}"
            holder.textViewMarketCap.text = "Market Cap: ${currentItem.marketCap}"

            val imageLoader = ImageLoader(requestQueue, ImageLoader.ImageCache {

                return@ImageCache null
            })



            val imageRequest = ImageLoader.ImageListener(
                holder.imageView,
                android.R.drawable.ic_menu_report_image,
                android.R.drawable.ic_menu_report_image
            )

            imageLoader.get(currentItem.imageUrl, imageRequest)

            return view!!

        }

        private inner class ViewHolder(view: View) {
            val imageView: ImageView = view.findViewById(R.id.imageView)
            val textViewId: TextView = view.findViewById(R.id.textViewId)
            val textViewSymbol: TextView = view.findViewById(R.id.textViewSymbol)
            val textViewName: TextView = view.findViewById(R.id.textViewName)
            val textViewPrice: TextView = view.findViewById(R.id.textViewPrice)
            val textViewMarketCap: TextView = view.findViewById(R.id.textViewMarketCap)
        }
    }


    }







