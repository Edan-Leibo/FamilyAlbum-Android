package com.example.adima.familyalbumproject.Controller.Albums;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.adima.familyalbumproject.Album.Model.Album;
import com.example.adima.familyalbumproject.Album.Model.AlbumListViewModel;
import com.example.adima.familyalbumproject.Controller.MainActivity;
import com.example.adima.familyalbumproject.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by adima on 03/03/2018.
 */

public class AlbumsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private static List<Album> albumList = new LinkedList<>();
    AlbumListAdapter adapter;
    ProgressBar progressBar;

    private AlbumListViewModel albumListViewModel;

    public AlbumsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EmployeeListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumsFragment newInstance(List<Album> albums) {
        AlbumsFragment fragment = new AlbumsFragment();
       // albumList = albums;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        ListView list = view.findViewById(R.id.albums_list);
        adapter = new AlbumListAdapter();
        list.setAdapter(adapter);
        progressBar = view.findViewById(R.id.album_list_progressbar);
        //MyTask task = new MyTask();
        progressBar.setVisibility(View.GONE);
        //task.execute("");

        view.findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).showLoginFragment();
            }
        });

        view.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((MainActivity) getActivity()).showCreateAlbumFragment();
                ((MainActivity) getActivity()).showCommentsFragment("-L6lY_t5pKGBZTTzoeJ0");
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        this.albumListViewModel = ViewModelProviders.of(this).get(AlbumListViewModel.class);
        albumListViewModel.getAlbumssList().observe(this, new Observer<List<Album>>() {
            @Override
            public void onChanged(@Nullable List<Album> albums) {
                albumList = albums;
                if (adapter != null) adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onItemSelected(Album album);
    }

    class AlbumListAdapter extends BaseAdapter {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @Override
        public int getCount() {
            return albumList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
/*DELETE??
        class CBListener implements View.OnClickListener{
            @Override
            public void onClick(View v) {
                int pos = (int)v.getTag();
                Album emp = albumList.get(pos);
                emp.checked = !emp.checked;
            }
        }*/

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null){   //On creation
                convertView = inflater.inflate(R.layout.album_list_row,null);
            }

            TextView albumName = (TextView) convertView.findViewById(R.id.album_list_row_name);
            TextView albumLocation = (TextView) convertView.findViewById(R.id.album_list_row_location);
            TextView albumDate = (TextView) convertView.findViewById(R.id.album_list_row_date);

            final Album alb = albumList.get(position);
            albumName.setText(alb.getName());
            albumLocation.setText(alb.getLocation());
            albumDate.setText(alb.getDate());

            return convertView;
        }
    }
}
