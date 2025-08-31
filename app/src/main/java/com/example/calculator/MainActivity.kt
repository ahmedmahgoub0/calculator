package com.example.calculator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.example.calculator.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars = true

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        addNumberButtonsCallbacks()
        addOperatorButtonsCallBacks()
        addRestOfButtonsCallbacks()

        observeState()
    }

    private fun addNumberButtonsCallbacks() {
        binding.one.setOnClickListener { viewModel.clickNumber("1") }
        binding.two.setOnClickListener { viewModel.clickNumber("2") }
        binding.three.setOnClickListener { viewModel.clickNumber("3") }
        binding.four.setOnClickListener { viewModel.clickNumber("4") }
        binding.five.setOnClickListener { viewModel.clickNumber("5") }
        binding.six.setOnClickListener { viewModel.clickNumber("6") }
        binding.seven.setOnClickListener { viewModel.clickNumber("7") }
        binding.eight.setOnClickListener { viewModel.clickNumber("8") }
        binding.nine.setOnClickListener { viewModel.clickNumber("9") }
        binding.zero.setOnClickListener { viewModel.clickNumber("0") }
        binding.point.setOnClickListener { viewModel.clickNumber(".") }
    }

    private fun addOperatorButtonsCallBacks() {
        binding.plus.setOnClickListener { viewModel.clickOperator(Operator.PLUS) }
        binding.minus.setOnClickListener { viewModel.clickOperator(Operator.MINUS) }
        binding.multiply.setOnClickListener { viewModel.clickOperator(Operator.MULTIPLY) }
        binding.division.setOnClickListener { viewModel.clickOperator(Operator.DIVIDE) }
        binding.percent.setOnClickListener { viewModel.clickOperator(Operator.MODULUS) }
        binding.changeSign.setOnClickListener { viewModel.clickOperator(Operator.CHANGE_SIGN) }
    }

    private fun addRestOfButtonsCallbacks() {
        binding.ac.setOnClickListener { viewModel.clearCalculations() }
        binding.clearChar.setOnClickListener { viewModel.clearLast() }
        binding.equal.setOnClickListener {
            try {
                viewModel.calculate()
            } catch (e: Exception) {
                Toast.makeText(this, "Cannot divide by zero", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                val sign = state.operator?.sign ?: ""
                binding.previousCalculation.text = state.previousResult
                binding.currentCalculation.text = state.firstNumber + sign + state.secondNumber
            }
        }
    }
}