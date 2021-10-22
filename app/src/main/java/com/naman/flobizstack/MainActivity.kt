package com.naman.flobizstack

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.naman.flobizstack.DBroom.QuestionDatabase
import com.naman.flobizstack.adapters.QuestionAdapter
import com.naman.flobizstack.databinding.ActivityHomeBinding
import com.naman.flobizstack.models.Item
import com.naman.news.repository.QuestionRepository
import com.naman.news.ui.QuestionViewModel
import com.naman.news.ui.QuestionViewModelProviderFactory
import com.naman.news.util.Constants
import com.naman.news.util.Resource
import kotlinx.coroutines.*
import android.app.Activity
import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.naman.flobizstack.adapters.BottomSheetAdapter
import android.net.NetworkInfo

import android.net.ConnectivityManager
import android.text.Editable
import android.text.TextWatcher


open class MainActivity : AppCompatActivity() {

    lateinit var questionAdapter: QuestionAdapter
    lateinit var bottomSheetAdapter: BottomSheetAdapter

    lateinit var viewModel: QuestionViewModel
    lateinit var binding: ActivityHomeBinding
    var ansCount: Int = 0
    var viewCount: Int = 0
    var title:String="android"
    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    var tagList: MutableList<String> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val questionRepository = QuestionRepository(QuestionDatabase(this))
        val viewModelProviderFactory = QuestionViewModelProviderFactory(questionRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(QuestionViewModel::class.java)

        var ifNetworkAvailable: Boolean = isNetworkAvailable()
        Log.d(TAG, "onCreate: "+ifNetworkAvailable)


        if (ifNetworkAvailable){
            viewModel.questions.observe(this, Observer { response ->

                when (response) {
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { questionRespnse ->
                            questionAdapter.differ.submitList(questionRespnse.items.toList())


//                        calculation of view count and average count in background thread
                            scope.launch {
                                (Dispatchers.Default) {
                                    for (i in questionRespnse.items.toList()) {
                                        tagList.addAll(i.tags)
                                        viewCount += i.view_count!!
                                        ansCount += i.answer_count!!
                                        viewModel.insert(i)
                                    }

                                    withContext(Dispatchers.Main) {

                                        binding.ansCount.text =
                                            (ansCount / (questionRespnse.items.toList().size)).toString()
                                        binding.viewCount.text =
                                            (viewCount / (questionRespnse.items.toList().size)).toString()
                                    }
                                }
                            }


                            val totalPages = questionRespnse.quota_max / Constants.QUERY_PAGE_SIZE + 2
                            isLastPage = viewModel.questionsPage == totalPages
                            if (isLastPage) {
                                binding.questionRv.setPadding(0, 0, 0, 0)
                            }
                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        response.message?.let { message ->
                            Log.e(ContentValues.TAG, "An error occured: $message")
                        }
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            })



        }else{
            viewModel.getItem().observe(this, Observer {

                questionAdapter.differ.submitList(it)

            })

        }

        binding.searchBarTv.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                title=s.toString()

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        setupRecyclerView()




        questionAdapter.setOnItemClickListener {
            val intent = Intent(this, QuestionWebView::class.java)
            intent.putExtra("url", it.link)
            startActivity(intent)
        }



        binding.filterIv.setOnClickListener {
            bottomsheet()
        }

    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false

    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    val scrolListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPos = layoutManager.findFirstVisibleItemPosition()
            val visItemCount = layoutManager.childCount
            val totalIteMCount = layoutManager.itemCount

            val isLoadingAndNotLastPage = !isLoading && !isLastPage

            val isLstItem = firstVisibleItemPos + visItemCount >= totalIteMCount
            val isNotAtBegining = firstVisibleItemPos >= 0
            val isTotalMoreThanVis = totalIteMCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate =
                isLoadingAndNotLastPage && isLstItem && isNotAtBegining && isTotalMoreThanVis && isScrolling

            if (shouldPaginate) {
                viewModel.getQuestions()
                isScrolling = false
            }
        }
    }

    private fun setupRecyclerView() {
        questionAdapter = QuestionAdapter()
        binding.questionRv.apply {
            adapter = questionAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addOnScrollListener(scrolListener)
        }
    }

    private fun bottomsheet() {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val bottomSheetView: View = (this as Activity).layoutInflater.inflate(
            R.layout.bottom_sheet,
            bottomSheetDialog.findViewById(R.id.layout_bottom_sheet_container)

        )
        val tagRv = bottomSheetView.findViewById<RecyclerView>(R.id.tagRv)

        bottomSheetAdapter = BottomSheetAdapter(tagList)
        tagRv.apply {
            adapter = bottomSheetAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    open fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    override fun onResume() {
        super.onResume()
    }


}
