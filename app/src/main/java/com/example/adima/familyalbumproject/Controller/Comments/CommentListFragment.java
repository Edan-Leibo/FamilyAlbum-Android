package com.example.adima.familyalbumproject.Controller.Comments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adima.familyalbumproject.Controller.Start.MyApplication;
import com.example.adima.familyalbumproject.Model.Entities.Comment.Comment;
import com.example.adima.familyalbumproject.Model.Entities.Comment.CommentListViewModel;
import com.example.adima.familyalbumproject.Model.Firebase.FirebaseAuthentication;
import com.example.adima.familyalbumproject.Model.Model.Model;
import com.example.adima.familyalbumproject.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class CommentListFragment extends Fragment {
    private OnFragmentCommentInteractionListener mListener;
    private String albumId;
    List<Comment> commentList = new LinkedList<>();
    CommentListAdapter adapter;
    ProgressBar commentsProgressBar;
    ListView list;
    private CommentListViewModel commentsListViewModel;

    public CommentListFragment() {
    }

    public interface OnFragmentCommentInteractionListener {
        void showAlbumFragment(String albumId);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EmployeeListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommentListFragment newInstance(String albumId) {
        CommentListFragment fragment = new CommentListFragment();
        Bundle args = new Bundle();
        args.putString("albumId", albumId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.comments_actionbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.btn_back_to_album:
                mListener.showAlbumFragment(albumId);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        commentsProgressBar = view.findViewById(R.id.commentList_progressbar);
        commentsProgressBar.setVisibility(View.VISIBLE);

        Button  commentBtn = view.findViewById(R.id.comment_btn);
        final EditText commentEditText   = view.findViewById(R.id.editText_comment);

        commentBtn .setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        final String text= commentEditText.getText().toString();
                        Log.v("EditText value=", text);
                        if (text.equals("")){
                            Toast.makeText(MyApplication.getMyContext(), "Empty message", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        commentsProgressBar.setVisibility(View.VISIBLE);
                        commentEditText.setText("");

                        final Comment comment =new Comment();
                        comment.setText(text);
                        comment.setAlbumId(albumId);
                        comment.setUserId(FirebaseAuthentication.getUserEmail());
                        Model.instance().addComment(albumId, comment, new Model.OnCreation() {
                            @Override
                            public void onCompletion(boolean success) {
                                Log.d("TAG","create comment"+success);
                                commentsProgressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                });

        list= view.findViewById(R.id.comment_listView);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Comment comment = commentList.get(i);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setMessage("Delete the comment?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                commentsProgressBar.setVisibility(View.VISIBLE);
                                Model.instance().removeComment(comment, new Model.OnRemove() {
                                    @Override
                                    public void onCompletion(boolean success) {
                                        if (success==true){
                                            //Toast.makeText(MyApplication.getMyContext(), "Successfully deleted", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(MyApplication.getMyContext(), "Could not delete the message", Toast.LENGTH_SHORT).show();
                                        }
                                        commentsProgressBar.setVisibility(View.GONE);
                                    }
                                });


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return  true;
            }
        });
        adapter = new CommentListAdapter();
        list.setAdapter(adapter);
        commentsProgressBar.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentCommentInteractionListener) {
            mListener = (OnFragmentCommentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        Bundle args = getArguments();
        albumId = args.getString("albumId", "");

        commentsListViewModel = ViewModelProviders.of(this).get(CommentListViewModel.class);
        commentsListViewModel.init(albumId);
        commentsListViewModel.getCommentsList().observe(this, new Observer<List<Comment>>() {
            @Override
            public void onChanged(@Nullable List<Comment> comments) {
                commentList = comments;
                if (adapter != null) adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        adapter=null;
        list.setAdapter(null);
        Model.instance().removeAllObserversFromComments();
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


    class CommentListAdapter extends BaseAdapter {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @Override
        public int getCount() {
            return commentList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }




        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.cell_comment, null);
            }
            //TextView messageText = (TextView)convertView.findViewById(R.id.message_text);
            TextView messageUser = (TextView)convertView.findViewById(R.id.message_user);
            TextView messageTime = (TextView)convertView.findViewById(R.id.message_time);

            TextView commentText = (TextView) convertView.findViewById(R.id.cell_comment_text);
            final Comment cmt = commentList.get(position);
            commentText.setText(cmt.getText());
            messageUser.setText(cmt.getUserId());
            Date d = new Date(cmt.getLastUpdated());


            DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            DateFormat dfFr = new SimpleDateFormat(
                    "dd-MM-yy");
            String m = dfFr.format(cmt.getLastUpdated());

            String dateFormatted = formatter.format(d);

            messageTime.setText(m+" "+"("+dateFormatted+")");
            return convertView;
        }
    }
}
