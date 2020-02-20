package com.selecttvapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.selecttvapp.R;
import com.selecttvapp.common.Constants;
import com.selecttvapp.presentation.activities.PresenterSearch;
import com.selecttvapp.presentation.views.ViewSearch;
import com.selecttvapp.ui.activities.SearchResultsActivity;
import com.selecttvapp.ui.bean.SearchResultBean;
import com.selecttvapp.ui.bean.SearchResultListBean;

import java.util.ArrayList;
import java.util.NoSuchElementException;


public class SearchFragment extends DialogFragment implements ViewSearch {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM_SEARCH_VALUE = "search_value";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private PresenterSearch presenter;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MY_DIALOG);
        presenter = new PresenterSearch(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        final EditText editSearch = (EditText) view.findViewById(R.id.editSearch);
        Button search = view.findViewById(R.id.btn_search);


        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        editSearch.requestFocus();

        focusChangelistener(editSearch);
        addTextChangelistener(editSearch);
        settouchlistener(editSearch);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(v.getWindowToken(), 0);
                String text = editSearch.getText().toString();
                presenter.loadSearchTask(text);
            }
        });
        editSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(v.getWindowToken(), 0);
                String text = editSearch.getText().toString();
                presenter.loadSearchTask(text);
            }
            return false;
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isVisible())
            dismiss();
    }

    @Override
    public void onSuccess(ArrayList<SearchResultListBean> searchList) {
        if (searchList != null) {
            if (isVisible())
                dismiss();
            Intent searchIntent = SearchResultsActivity.getIntent(getActivity(), searchList);
            getActivity().startActivityForResult(searchIntent, Constants.REQUEST_CODE_SEARCH);
        }
    }

    @Override
    public void onError(Exception e) {
        if (e instanceof NoSuchElementException)
            Toast.makeText(getActivity(), "No results found", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadItem(SearchResultBean item) {

    }


    private void focusChangelistener(final EditText editfocus) {
        final Drawable x = getDrawableX();
        editfocus.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                editfocus.setCompoundDrawables(null, null, editfocus.getText().toString().equals("") ? null : x, null);
            } else {
                editfocus.setCompoundDrawables(null, null, null, null);
            }
        });
    }

    private void addTextChangelistener(final EditText editext) {
        final Drawable x = getDrawableX();
        editext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editext.setCompoundDrawables(null, null, editext.getText().toString().equals("") ? null : x, null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editext.setCompoundDrawables(null, null, editext.getText().toString().equals("") ? null : x, null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                editext.setCompoundDrawables(null, null, editext.getText().toString().equals("") ? null : x, null);
            }
        });
    }

    private void settouchlistener(final EditText edit) {
        final Drawable x = getDrawableX();
        edit.setOnTouchListener((v, event) -> {
            //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            if (edit.getCompoundDrawables()[2] == null) {
                return false;
            }
            if (event.getAction() != MotionEvent.ACTION_UP) {
                return false;
            }
            if (event.getX() > edit.getWidth() - edit.getPaddingRight() - x.getIntrinsicWidth()) {
                edit.setText("");
                edit.setCompoundDrawables(null, null, null, null);
            }
            return false;
        });
    }

    private Drawable getDrawableX() {
        Drawable x = getResources().getDrawable(R.drawable.clear_icon);
        x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());
        return x;
    }
}
