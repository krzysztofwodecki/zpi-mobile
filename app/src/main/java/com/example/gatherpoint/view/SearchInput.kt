package com.example.gatherpoint.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gatherpoint.R
import com.example.gatherpoint.databinding.SearchInputBinding

class SearchInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :  CardView(context, attrs, defStyleAttr) {

    private val binding = SearchInputBinding.inflate(LayoutInflater.from(context), this)


    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    init {
        LayoutInflater.from(context).inflate(R.layout.search_input, this, true)
        initLayoutFromAttributes(attrs)

        binding.searchInput.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                _searchQuery.value = newText ?: ""
                return false
            }
        })
    }

    private fun initLayoutFromAttributes(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.SearchInput,
                0,
                0
            )

            val searchInputHintResId =
                typedArray.getResourceId(R.styleable.SearchInput_searchInputHint, -1)
                    .takeIf { it > -1 }

            binding.searchInput.apply {
                searchInputHintResId?.let {
                    queryHint = resources.getString(it)
                }
            }
        }
    }
}