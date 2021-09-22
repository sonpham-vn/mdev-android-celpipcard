package com.example.celpipcard.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.celpipcard.R

class CardListAdapter(private val context: Activity, private val content: Array<String>, private val pretime: Array<Int?>, private val spktime: Array<Int?>)
    : ArrayAdapter<String>(context, R.layout.card_custom_view, content) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.card_custom_view, null, true)

        val contentText = rowView.findViewById(R.id.contentText) as TextView
        val pretimeText = rowView.findViewById(R.id.pretimeText) as TextView
        val spktimeText = rowView.findViewById(R.id.spktimeText) as TextView

        contentText.text = "${content[position]}"
        pretimeText.text = "Prepare Time: ${pretime[position]}"
        spktimeText.text = "Speak Time: ${spktime[position]}"
        return rowView
    }
}
