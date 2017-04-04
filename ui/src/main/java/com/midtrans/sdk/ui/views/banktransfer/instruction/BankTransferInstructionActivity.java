package com.midtrans.sdk.ui.views.banktransfer.instruction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.constants.PaymentType;
import com.midtrans.sdk.ui.views.instructions.InstructionATMBersamaFragment;
import com.midtrans.sdk.ui.views.instructions.InstructionAltoFragment;
import com.midtrans.sdk.ui.views.instructions.InstructionBCAFragment;
import com.midtrans.sdk.ui.views.instructions.InstructionBCAKlikFragment;
import com.midtrans.sdk.ui.views.instructions.InstructionBCAMobileFragment;
import com.midtrans.sdk.ui.views.instructions.InstructionMandiriFragment;
import com.midtrans.sdk.ui.views.instructions.InstructionMandiriInternetFragment;
import com.midtrans.sdk.ui.views.instructions.InstructionPermataFragment;
import com.midtrans.sdk.ui.views.instructions.InstructionPrimaFragment;
import com.midtrans.sdk.ui.widgets.DefaultTextView;

/**
 * Created by ziahaqi on 4/4/17.
 */

public class BankTransferInstructionActivity extends BaseActivity {
    public static final String DOWNLOAD_URL = "url";
    public static final String BANK = "bank";
    public static final String TYPE_BCA = "bank.bca";
    public static final String TYPE_PERMATA = "bank.permata";
    public static final String TYPE_MANDIRI = "bank.mandiri";
    public static final String TYPE_MANDIRI_BILL = "bank.mandiri.bill";
    public static final String TYPE_ALL_BANK = "bank.others";
    public static final String PAGE = "page";
    public static final int KLIKBCA_PAGE = 1;
    private static final int PAGE_MARGIN = 20;
    private int POSITION = -1;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Button downloadInstruction;
    private DefaultTextView textHeaderTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banktransfer_instruction);
        initializeViews();
        setupViews();
    }

    private void setupViews() {

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (!TextUtils.isEmpty(getIntent().getStringExtra(DOWNLOAD_URL))) {
            downloadInstruction.setVisibility(View.VISIBLE);
            downloadInstruction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW);
                    webIntent.setData(Uri.parse(getIntent().getStringExtra(DOWNLOAD_URL)));
                    startActivity(webIntent);
                }
            });
        } else {
            downloadInstruction.setVisibility(View.GONE);
        }

        textHeaderTitle.setText(getResources().getString(R.string.payment_instrution));
        setUpViewPager();
        setUpTabLayout();
    }


    /**
     * handles click of back arrow given on action bar.
     *
     * @param item selected menu
     * @return is handled or not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            //close activity on click of cross button.
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * set up action bar, view pager and tabs.
     */
    private void initializeViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        textHeaderTitle = (DefaultTextView) findViewById(R.id.title_header);
        tabLayout = (TabLayout) findViewById(R.id.instruction_tabs);
        viewPager = (ViewPager) findViewById(R.id.pager_bank_instruction);
        downloadInstruction = (Button) findViewById(R.id.btn_download_instruction);
    }


    private void setUpTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


    /**
     * set adapter to view pager and also adds page margin between view pages.
     */
    private void setUpViewPager() {
        viewPager.setPageMargin(PAGE_MARGIN);
        int pageNumber;
        switch (getIntent().getStringExtra(BANK)) {
            case PaymentType.BCA_VA:
                pageNumber = 3;
                POSITION = getIntent().getIntExtra(PAGE, -1);
                break;
            case PaymentType.PERMATA_VA:
                pageNumber = 2;
                break;
            case PaymentType.E_CHANNEL:
                pageNumber = 2;
                break;
            case PaymentType.OTHER_VA:
                pageNumber = 3;
                break;
            default:
                pageNumber = 0;
                break;
        }
        InstructionFragmentPagerAdapter adapter = new InstructionFragmentPagerAdapter(
                getSupportFragmentManager(), pageNumber);
        viewPager.setAdapter(adapter);
        if (POSITION > -1) {
            viewPager.setCurrentItem(POSITION);
        }
    }


    /**
     * adapter for view pager which will provide instruction fragment depending upon tab click and
     * view pager swap position.
     */
    public class InstructionFragmentPagerAdapter extends FragmentStatePagerAdapter {

        /**
         * number of pages / tabs.
         */
        private final int pages;

        public InstructionFragmentPagerAdapter(FragmentManager fragmentManager, int number) {
            super(fragmentManager);
            this.pages = number;
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment;

            if (getIntent().getStringExtra(BANK).equals(TYPE_BCA)) {
                if (position == 0) {
                    fragment = new InstructionBCAFragment();
                } else if (position == 1) {
                    fragment = new InstructionBCAKlikFragment();
                } else fragment = new InstructionBCAMobileFragment();

            } else if (getIntent().getStringExtra(BANK).equals(TYPE_PERMATA)) {
                fragment = new InstructionPermataFragment();

            } else if (getIntent().getStringExtra(BANK).equals(TYPE_MANDIRI) ||
                    getIntent().getStringExtra(BANK).equals(TYPE_MANDIRI_BILL)) {
                if (position == 0) {
                    fragment = new InstructionMandiriFragment();
                } else {
                    fragment = new InstructionMandiriInternetFragment();
                }
            } else {
                if (position == 0) {
                    fragment = new InstructionATMBersamaFragment();
                } else if (position == 1) {
                    fragment = new InstructionPrimaFragment();
                } else {
                    fragment = new InstructionAltoFragment();
                }
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return pages;
        }


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


        @Override
        public CharSequence getPageTitle(int position) {

            if (getIntent().getStringExtra(BANK).equals(TYPE_BCA)) {
                if (position == 0) {
                    return getString(R.string.tab_bca_atm);
                } else if (position == 1) {
                    return getString(R.string.tab_bca_klik);
                } else return getString(R.string.tab_bca_mobile);
            } else if (getIntent().getStringExtra(BANK).equals(TYPE_PERMATA)) {
                if (position == 0) return getString(R.string.tab_permata_atm);
                else return getString(R.string.tab_alto);
            } else if (getIntent().getStringExtra(BANK).equals(TYPE_MANDIRI_BILL) ||
                    getIntent().getStringExtra(BANK).equals(TYPE_MANDIRI)) {
                if (position == 0) {
                    return getString(R.string.tab_mandiri_atm);
                } else {
                    return getString(R.string.tab_mandiri_internet);
                }
            } else {
                if (position == 0) {
                    return getString(R.string.tab_atm_bersama);
                } else if (position == 1) {
                    return getString(R.string.tab_prima);
                } else {
                    return getString(R.string.tab_alto);
                }
            }
        }
    }
}
