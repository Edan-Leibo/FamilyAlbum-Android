package com.example.adima.familyalbumproject.Controller.Comments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adima.familyalbumproject.MyApplication;
import com.example.adima.familyalbumproject.R;

import java.util.LinkedList;
import java.util.List;

import Model.Entities.Comment.Comment;
import Model.Entities.Comment.CommentListViewModel;
import Model.Entities.User.User;
import Model.Firebase.FirebaseAuthentication;
import Model.Model;


public class CommentListFragment extends Fragment {
    private OnFragmentCommentInteractionListener mListener;
    private String albumId;
    List<Comment> commentList = new LinkedList<>();
    CommentListAdapter adapter;
    ProgressBar progressBar;
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
                        commentEditText.setText("");

                        Model.instance().getUserProfilePicture(new Model.GetKeyListener() {
                            @Override
                            public void onCompletion(String success) {
                               final Comment comment =new Comment();
                                comment.setText(text);
                                comment.setAlbumId(albumId);
                                comment.setUserId(FirebaseAuthentication.getUserEmail());


                                if(success!=null) {
                                    Log.d("TAG","suc not null");
                                    Log.d("TAG","suc"+success);
                                    comment.setImageUrl(success);
                                }
                                else{
                                    //Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
                                   // notBuilder.setLargeIcon(largeIcon);
                                    Drawable myDrawable = getResources().getDrawable(R.drawable.avatar);
                                    Bitmap imageBitmap = ((BitmapDrawable) myDrawable).getBitmap();

                                    if (imageBitmap != null) {
                                        Model.instance().saveImage(imageBitmap, FirebaseAuthentication.getUserEmail(), new Model.SaveImageListener() {
                                            @Override
                                            public void complete(String url) {

                                                User user = new User();
                                                user.setEmailUser(FirebaseAuthentication.getUserEmail());
                                                user.setImageUrl(url);
                                                comment.setImageUrl(url);
                                                Log.d("TAG","THE URL OF THE USER "+user.getImageUrl());
                                                Model.instance().addUserProfilePicture(user, new Model.OnCreation() {
                                                    @Override
                                                    public void onCompletion(boolean success) {
                                                        if (success) {
                                                            Toast.makeText(MyApplication.getMyContext(), "Photo was saved successfully", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(MyApplication.getMyContext(), "Failed to save photo", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }

                                            @Override
                                            public void fail() {
                                                Toast.makeText(MyApplication.getMyContext(), "Failed to save photo", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(MyApplication.getMyContext(), "Error", Toast.LENGTH_SHORT).show();
                                    }



                                }

                                Model.instance().addComment(albumId, comment, new Model.OnCreation() {
                                    @Override
                                    public void onCompletion(boolean success) {
                                        Log.d("TAG",""+success);
                                    }
                                });
                            }
                        });

                    }
                });

        ListView list = view.findViewById(R.id.comment_listView);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Comment comment = commentList.get(i);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setMessage("Delete the comment?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Model.instance().removeComment(comment, new Model.OnRemove() {
                                    @Override
                                    public void onCompletion(boolean success) {
                                        if (success==true){
                                            Toast.makeText(MyApplication.getMyContext(), "Successfully deleted", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(MyApplication.getMyContext(), "could not delete the image", Toast.LENGTH_SHORT).show();
                                        }

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
        progressBar = view.findViewById(R.id.commentList_progressbar);
        progressBar.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentCommentInteractionListener) {
            mListener = (OnFragmentCommentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
            //        + " must implement OnFragmentInteractionListener");
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
            TextView commentText = (TextView) convertView.findViewById(R.id.cell_comment_text);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.cell_comment_user_image);
            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.comment_cell_progressBar);
            final Comment cmt = commentList.get(position);
            commentText.setText(cmt.getText());
            imageView.setTag(cmt.getImageUrl());
            //imageView.setImageDrawable(getContext().getDrawable(R.drawable.avatar));
            if (cmt.getImageUrl() != null && !cmt.getImageUrl().isEmpty() && !cmt.getImageUrl().equals("")) {
                progressBar.setVisibility(View.VISIBLE);
                Model.instance().getImage(cmt.getImageUrl(), new Model.GetImageListener() {
                    @Override
                    public void onSuccess(Bitmap image) {
                        String tagUrl = imageView.getTag().toString();
                        if (tagUrl.equals(cmt.getImageUrl())) {
                            imageView.setImageBitmap(image);
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFail() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
            return convertView;
        }
    }
}
