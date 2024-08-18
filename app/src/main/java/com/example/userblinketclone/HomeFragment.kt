package com.example.userblinketclone

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.userblinketclone.adapters.AdapterCategory
import com.example.userblinketclone.databinding.FragmentHomeBinding
import com.example.userblinketclone.models.Category

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setStatusBarColor(R.color.color_primary)
        setAllcategories()


        return binding.root
    }

    private fun setAllcategories() {
        val categoryList = ArrayList<Category>()

        for(i in 0 until Constants.allProductCatgoryIcon.size)

            categoryList.add(Category(Constants.allProductCatgoryName[i],Constants.allProductCatgoryIcon[i]))
        binding.rvShopByCategories.adapter = AdapterCategory(categoryList)


    }

    private fun Fragment.setStatusBarColor(colorResId: Int) {
        activity?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = it.window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(requireContext(), colorResId)
            }
        }
    }


}