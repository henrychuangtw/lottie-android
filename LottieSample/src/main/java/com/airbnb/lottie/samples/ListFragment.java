package com.airbnb.lottie.samples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment {

    static ListFragment newInstance() {
        return new ListFragment();
    }

    @BindView(R.id.container) ViewGroup container;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private final FileAdapter adapter = new FileAdapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        recyclerView.setAdapter(adapter);
        String[] files = null;
        try {
            adapter.setFiles(getContext().getAssets().list(""));
        } catch (IOException e) {
            //noinspection ConstantConditions
            Snackbar.make(container, R.string.invalid_assets, Snackbar.LENGTH_LONG).show();
        }

        return view;
    }

    private void onFileClicked(String fileName) {
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.slide_in_right, R.anim.hold, R.anim.hold, R.anim.slide_out_right)
                .remove(this)
                .replace(R.id.content_2, AnimationFragment.newInstance(fileName))
                .commit();
    }

    private void onGridClicked() {
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.slide_in_right, R.anim.hold, R.anim.hold, R.anim.slide_out_right)
                .remove(this)
                .replace(R.id.content_2, GridFragment.newInstance())
                .commit();
    }

    final class FileAdapter extends RecyclerView.Adapter<StringViewHolder> {
        static final int VIEW_TYPE_GRID = 1;
        static final int VIEW_TYPE_FILE = 2;

        @Nullable private String[] files = null;

        void setFiles(@Nullable String[] files) {
            this.files = files;
            notifyDataSetChanged();
        }

        @Override
        public StringViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new StringViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(StringViewHolder holder, int position) {
            if (holder.getItemViewType() == VIEW_TYPE_GRID) {
                holder.bind("Grid");
            } else {
                //noinspection ConstantConditions
                holder.bind(files[position - 1]);
            }
        }

        @Override
        public int getItemCount() {
            return (files == null ? 0 : files.length) + 1;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? VIEW_TYPE_GRID : VIEW_TYPE_FILE;
        }
    }

    final class StringViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.file_name) TextView fileNameView;

        StringViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_file, parent, false));
            ButterKnife.bind(this, itemView);
        }

        void bind(final String name) {
            fileNameView.setText(name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getItemViewType() == FileAdapter.VIEW_TYPE_GRID) {
                        onGridClicked();
                    } else {
                        onFileClicked(name);
                    }
                }
            });
        }
    }
}