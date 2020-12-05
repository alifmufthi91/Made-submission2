package com.example.moviecatalogue.search.menu


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.moviecatalogue.R
import com.example.moviecatalogue.data.source.local.entity.GenreEntity
import com.example.moviecatalogue.search.SearchByGenreActivity
import com.example.moviecatalogue.search.SearchByGenreActivity.Companion.SELECTED_CATEGORY
import com.example.moviecatalogue.search.SearchByGenreActivity.Companion.SELECTED_GENRE
import com.example.moviecatalogue.search.SearchViewModel
import com.example.moviecatalogue.shows.movie.MovieFragment.Companion.SHOW_MOVIE
import com.example.moviecatalogue.shows.tv.TvFragment.Companion.SHOW_TV
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_search_menu.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SearchMenuFragment : DaggerFragment() {

    @Inject
    lateinit var searchViewModel: SearchViewModel
    private lateinit var adapter: ArrayAdapter<String>

    companion object;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_menu, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter =
            object : ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val customTv = super.getView(position, convertView, parent) as TextView
                    customTv.setTextColor(ContextCompat.getColor(context, R.color.textPrimary))
                    return customTv
                }
            }
        lv_genre.adapter = adapter
        val observer = Observer<List<GenreEntity>>{
            if (it != null) {
                adapter.clear()
                for (genre in it.toList()) {
                    adapter.add(genre.name)
                }
                adapter.notifyDataSetChanged()
            }
        }
        rg_category.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_movie -> {
                    searchViewModel.setCategory(SHOW_MOVIE)
                    searchViewModel.setGenres()
                    searchViewModel.getGenres().observe(viewLifecycleOwner, observer)
                    Log.d("radio", "movie")
                }
                R.id.radio_tv -> {
                    searchViewModel.setCategory(SHOW_TV)
                    searchViewModel.setGenres()
                    searchViewModel.getGenres().observe(viewLifecycleOwner, observer)
                    Log.d("radio", "tv")
                }
            }
        }
        searchViewModel.setGenres()
        searchViewModel.getGenres().observeForever(observer)
        lv_genre.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val intent = Intent(context, SearchByGenreActivity::class.java)
                intent.putExtra(SELECTED_CATEGORY, searchViewModel.getCategory())
                intent.putExtra(SELECTED_GENRE,
                    searchViewModel.getGenres().value?.get(position)
                )
                startActivity(intent)
            }

    }

}
