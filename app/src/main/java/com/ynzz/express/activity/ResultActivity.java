package com.ynzz.express.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.ynzz.express.adapter.ResultAdapter;
import com.ynzz.express.constants.Extras;
import com.ynzz.express.http.HttpCallback;
import com.ynzz.express.http.HttpClient;
import com.ynzz.express.model.SearchInfo;
import com.ynzz.express.model.SearchResult;
import com.ynzz.express.utils.DataManager;
import com.ynzz.express.utils.SnackbarUtils;
import com.ynzz.express.utils.binding.Bind;
import com.ynzz.gaodemap.R;


public class ResultActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ResultActivity";
    @Bind(R.id.iv_logo)
    private ImageView ivLogo;
    @Bind(R.id.tv_post_id)
    private TextView tvPostId;
    @Bind(R.id.tv_name)
    private TextView tvName;
    @Bind(R.id.ll_result)
    private LinearLayout llResult;
    @Bind(R.id.lv_result_list)
    private ListView lvResultList;
    @Bind(R.id.btn_remark)
    private Button btnRemark;
    @Bind(R.id.ll_no_exist)
    private LinearLayout llNoExist;
    @Bind(R.id.btn_save)
    private Button btnSave;
    @Bind(R.id.ll_error)
    private LinearLayout llError;
    @Bind(R.id.btn_retry)
    private Button btnRetry;
    @Bind(R.id.tv_searching)
    private TextView tvSearching;
    private SearchInfo mSearchInfo;

    public static void start(Context context, SearchInfo searchInfo) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra(Extras.SEARCH_INFO, searchInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        mSearchInfo = (SearchInfo) intent.getSerializableExtra(Extras.SEARCH_INFO);
        Glide.with(this)
                .load(HttpClient.urlForLogo(mSearchInfo.getLogo()))
                .dontAnimate()
                .placeholder(R.drawable.ic_default_logo)
                .into(ivLogo);
        refreshSearchInfo();

        query();
    }

    @Override
    protected void setListener() {
        btnRemark.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnRetry.setOnClickListener(this);
    }

    private void refreshSearchInfo() {
        String remark = DataManager.getInstance().getRemark(mSearchInfo.getPost_id());
        if (TextUtils.isEmpty(remark)) {
            tvName.setText(mSearchInfo.getName());
            tvPostId.setText(mSearchInfo.getPost_id());
        } else {
            tvName.setText(remark);
            tvPostId.setText(mSearchInfo.getName() + " " + mSearchInfo.getPost_id());
        }
    }

    private void query() {
        HttpClient.query(mSearchInfo.getCode(), mSearchInfo.getPost_id(), new HttpCallback<SearchResult>() {
            @Override
            public void onResponse(SearchResult searchResult) {
                Log.i(TAG, searchResult.getMessage());
                onSearch(searchResult);
            }

            @Override
            public void onError(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage(), volleyError);
                llResult.setVisibility(View.GONE);
                llNoExist.setVisibility(View.GONE);
                llError.setVisibility(View.VISIBLE);
                tvSearching.setVisibility(View.GONE);
            }
        });
    }

    private void onSearch(SearchResult searchResult) {
        if (searchResult.getStatus().equals("200")) {
            llResult.setVisibility(View.VISIBLE);
            llNoExist.setVisibility(View.GONE);
            llError.setVisibility(View.GONE);
            tvSearching.setVisibility(View.GONE);
            lvResultList.setAdapter(new ResultAdapter(searchResult));
            mSearchInfo.setIs_check(searchResult.getIscheck());
            DataManager.getInstance().updateHistory(mSearchInfo);
        } else {
            llResult.setVisibility(View.GONE);
            llNoExist.setVisibility(View.VISIBLE);
            llError.setVisibility(View.GONE);
            tvSearching.setVisibility(View.GONE);
            btnSave.setText(DataManager.getInstance().idExists(mSearchInfo.getPost_id()) ? "运单备注" : "保存运单信息");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_remark:
                remark();
                break;
            case R.id.btn_save:
                if (TextUtils.equals(btnSave.getText().toString(), "运单备注")) {
                    remark();
                } else {
                    mSearchInfo.setIs_check("0");
                    DataManager.getInstance().updateHistory(mSearchInfo);
                    SnackbarUtils.show(this, "保存成功");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!ResultActivity.this.isFinishing()) {
                                startActivity(new Intent(ResultActivity.this, ExpressActivity.class));
                            }
                        }
                    }, 1000);
                }
                break;
            case R.id.btn_retry:
                llResult.setVisibility(View.GONE);
                llNoExist.setVisibility(View.GONE);
                llError.setVisibility(View.GONE);
                tvSearching.setVisibility(View.VISIBLE);
                query();
                break;
        }
    }

    private void remark() {
        View view = getLayoutInflater().inflate(R.layout.dialog_result, null);
        final EditText etRemark = (EditText) view.findViewById(R.id.et_remark);
        etRemark.setText(DataManager.getInstance().getRemark(mSearchInfo.getPost_id()));
        etRemark.setSelection(etRemark.length());
        new AlertDialog.Builder(this)
                .setTitle("备注名")
                .setView(view)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataManager.getInstance().updateRemark(mSearchInfo.getPost_id(), etRemark.getText().toString());
                        refreshSearchInfo();
                        SnackbarUtils.show(ResultActivity.this, "备注成功");
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
