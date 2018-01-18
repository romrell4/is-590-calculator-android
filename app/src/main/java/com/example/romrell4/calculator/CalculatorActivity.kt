package com.example.romrell4.calculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.net.URLEncoder

class CalculatorActivity : AppCompatActivity() {
    private val numButtonIds = listOf(R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six,
            R.id.seven, R.id.eight, R.id.nine, R.id.dot)
    private val opButtonIds = listOf(R.id.add, R.id.subtract, R.id.multiply, R.id.divide)
    private val queue: RequestQueue
        get() = Volley.newRequestQueue(this)
    private var textView: TextView? = null
    private var answerDisplayed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        textView = findViewById<TextView>(R.id.textView)

        //Setup the click for the regular buttons
        numButtonIds.map { findViewById<Button>(it) }.forEach {
            it.setOnClickListener { _ ->
                //Number operators will clear after a result is done
                if (answerDisplayed) {
                    textView?.text = ""
                }
                answerDisplayed = false
                textView?.append(it.text)
            }
        }
        opButtonIds.map { findViewById<Button>(it) }.forEach {
            it.setOnClickListener { _ ->
                answerDisplayed = false
                textView?.append(it.text)
            }
        }

        //Setup the click for the enter button
        findViewById<Button>(R.id.enter).setOnClickListener {
            val query = URLEncoder.encode(textView?.text.toString(), "UTF-8")
            val request = StringRequest(Request.Method.GET, "http://api.mathjs.org/v1/?expr=$query", {
                answerDisplayed = true
                textView?.text = it
            }, {
                println(it.message)
            })
            queue.add(request)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.clear) {
            textView?.text = ""
        }
        return super.onOptionsItemSelected(item)
    }
}
