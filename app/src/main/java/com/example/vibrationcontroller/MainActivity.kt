package com.example.vibrationcontroller

import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var seekBar: SeekBar
    private lateinit var tvIntensity: TextView
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar = findViewById(R.id.seekBar_intensity)
        tvIntensity = findViewById(R.id.tv_intensity)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Load saved intensity (default 50)
        val prefs = getSharedPreferences("VibrationPrefs", Context.MODE_PRIVATE)
        val savedIntensity = prefs.getInt("intensity", 50)
        seekBar.progress = savedIntensity
        updateIntensityText(savedIntensity)

        // Slider change: Update text and save
        seekBar.setOnSeekBarChangeListener { _, progress, _ ->
            updateIntensityText(progress)
            prefs.edit().putInt("intensity", progress).apply()
        }

        // Test button
        findViewById<Button>(R.id.btn_test_vibration).setOnClickListener {
            testVibration()
        }
    }

    private fun updateIntensityText(progress: Int) {
        tvIntensity.text = "Vibration Intensity: ${progress}%"
    }

    private fun testVibration() {
        val intensity = seekBar.progress
        if (intensity == 0) return

        val amplitude = (intensity * 255 / 100).toLong()
        val waveform = longArrayOf(100)  // 100ms vibration
        val amplitudes = longArrayOf(amplitude)

        val effect = VibrationEffect.createWaveform(waveform, amplitudes, -1)
        vibrator.vibrate(effect)
    }
}
