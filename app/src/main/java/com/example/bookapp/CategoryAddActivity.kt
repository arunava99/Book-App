package com.example.bookapp

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bookapp.databinding.ActivityCatagoryAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CategoryAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatagoryAddBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var progressDailog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatagoryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDailog = ProgressDialog(this)
        progressDailog.setTitle("Please wait...")
        progressDailog.setCanceledOnTouchOutside(false)

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.submitBtn.setOnClickListener {
            validatedata()
        }

    }

    private var category = ""

    private fun validatedata() {

        category = binding.categoryEt.text.toString().trim()

        if (category.isEmpty()){
            Toast.makeText(this,"Enter category...",Toast.LENGTH_SHORT).show()
        }
        else{
            addCategoryFirebase()
        }
    }

    private fun addCategoryFirebase() {
        progressDailog.show()

        val timestamp = System.currentTimeMillis()

        val hashMap = hashMapOf<String,Any>()
        hashMap["id"] = "$timestamp"
        hashMap["timestamp"] = timestamp
        hashMap["category"]= category
        hashMap["uid" ] = "${firebaseAuth.uid}"

        val ref = FirebaseDatabase.getInstance().getReference("categories")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDailog.dismiss()
                Toast.makeText(this,"Added Successfully....",Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                progressDailog.dismiss()
                Toast.makeText(this,"Failed to add....",Toast.LENGTH_SHORT).show()
            }
    }


}