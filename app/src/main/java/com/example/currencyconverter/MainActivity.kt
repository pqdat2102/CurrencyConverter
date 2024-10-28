package com.example.currencyconverter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var etSourceAmount: EditText
    private lateinit var etDestinationAmount: EditText
    private lateinit var spinnerSourceCurrency: Spinner
    private lateinit var spinnerDestinationCurrency: Spinner

    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.94,
        "VND" to 24300.0,
        "JPY" to 148.5,
        "GBP" to 0.82
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        etSourceAmount = findViewById(R.id.etSourceAmount)
        etDestinationAmount = findViewById(R.id.etDestinationAmount)
        spinnerSourceCurrency = findViewById(R.id.spinnerSourceCurrency)
        spinnerDestinationCurrency = findViewById(R.id.spinnerDestinationCurrency)
        val currencyList = exchangeRates.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerSourceCurrency.adapter = adapter
        spinnerDestinationCurrency.adapter = adapter
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        etSourceAmount.addTextChangedListener(textWatcherWhenChangeTextInEdittext1)
        etDestinationAmount.addTextChangedListener(textWatcherWhenChangeTextInEdittext2)
        spinnerSourceCurrency.onItemSelectedListener = this
        spinnerDestinationCurrency.onItemSelectedListener = this
    }
    var textWatcherWhenChangeTextInEdittext1: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val numberInEditText1 = etSourceAmount.text.toString()
            if (numberInEditText1.isEmpty()) {
                etDestinationAmount.removeTextChangedListener(textWatcherWhenChangeTextInEdittext2)
                etDestinationAmount.setText("")
                etDestinationAmount.addTextChangedListener(textWatcherWhenChangeTextInEdittext2)
                return
            }
            val number1 = numberInEditText1.toDoubleOrNull() ?: 0.0
            val firstCurrency = spinnerSourceCurrency.selectedItem.toString()
            val secondCurrency = spinnerDestinationCurrency.selectedItem.toString()
            val firstRate = exchangeRates[firstCurrency] ?: 1.0
            val secondRate = exchangeRates[secondCurrency] ?: 1.0
            val convertedAmount = number1 * (secondRate / firstRate)
            etDestinationAmount.removeTextChangedListener(textWatcherWhenChangeTextInEdittext2)
            etDestinationAmount.setText(String.format("%.4f", convertedAmount))
            etDestinationAmount.addTextChangedListener(textWatcherWhenChangeTextInEdittext2)
        }

        override fun afterTextChanged(s: Editable) {}
    }

    var textWatcherWhenChangeTextInEdittext2: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val numberInEditText2 = etDestinationAmount.text.toString()
            if (numberInEditText2.isEmpty()) {
                etSourceAmount.removeTextChangedListener(textWatcherWhenChangeTextInEdittext1)
                etSourceAmount.setText("")
                etSourceAmount.addTextChangedListener(textWatcherWhenChangeTextInEdittext1)
                return
            }
            val number2 = numberInEditText2.toDoubleOrNull() ?: 0.0
            val firstCurrency = spinnerSourceCurrency.selectedItem.toString()
            val secondCurrency = spinnerDestinationCurrency.selectedItem.toString()
            val firstRate = exchangeRates[firstCurrency] ?: 1.0
            val secondRate = exchangeRates[secondCurrency] ?: 1.0
            val convertedAmount = number2 * (firstRate / secondRate)
            etSourceAmount.removeTextChangedListener(textWatcherWhenChangeTextInEdittext1)
            etSourceAmount.setText(String.format("%.4f", convertedAmount))
            etSourceAmount.addTextChangedListener(textWatcherWhenChangeTextInEdittext1)
        }

        override fun afterTextChanged(s: Editable) {}
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        updateConversion()
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
    private fun updateConversion() {
        val sourceAmountText = etSourceAmount.text.toString()
        val destinationAmountText = etDestinationAmount.text.toString()
        if (sourceAmountText.isEmpty() && destinationAmountText.isEmpty())
        {
            return
        }
        val sourceAmount = if (sourceAmountText.isNotEmpty()) sourceAmountText.toDouble() else 0.0
        val sourceCurrency = spinnerSourceCurrency.selectedItem.toString()
        val destinationCurrency = spinnerDestinationCurrency.selectedItem.toString()
        val sourceRate = exchangeRates[sourceCurrency] ?: 1.0
        val destinationRate = exchangeRates[destinationCurrency] ?: 1.0
        val convertedAmount = sourceAmount * (destinationRate / sourceRate)
        etDestinationAmount.setText(String.format("%.4f", convertedAmount))
    }
}