package com.example.cateringapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private DatabaseReference reference;
    private String userID;
    private StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;

    private CircleImageView showPhoto;
    private TextView uName, clgName, clgID, uEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        showPhoto = view.findViewById(R.id.showProfilePhoto);
        CircleImageView addPhoto = view.findViewById(R.id.addNewProfilePhoto);

        uName = view.findViewById(R.id.showUserName);
        clgName = view.findViewById(R.id.showUserCollegeName);
        clgID = view.findViewById(R.id.showUserCollegeIDNumber);
        uEmail = view.findViewById(R.id.showUserEmailAddress);

        Button updateProfileInfo = view.findViewById(R.id.updateUserProfile);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

        storageReference = FirebaseStorage.getInstance().getReference("uploads").child(userID);

        addPhoto.setOnClickListener(v -> openImage());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                uName.setText(user.getfName());
                clgName.setText(user.getcName());
                clgID.setText(user.getIDNumber());
                uEmail.setText(user.getEmailID());

                if (user.getProfilePhoto().equals("default")){
                    showPhoto.setImageResource(R.drawable.profile_pic);
                }else {
                    Glide.with(Objects.requireNonNull(getContext())).load(user.getProfilePhoto()).into(showPhoto);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateProfileInfo.setOnClickListener(v -> {
            final DialogPlus dialogOne = DialogPlus.newDialog(Objects.requireNonNull(getContext()))
                    .setGravity(Gravity.CENTER)
                    .setMargin(50, 0, 50, 0)
                    .setContentHolder(new ViewHolder(R.layout.profile_update_view))
                    .setExpanded(false)
                    .create();

            View holderView = dialogOne.getHolderView();

            final TextInputLayout name = holderView.findViewById(R.id.updateProfileName);
            final TextInputLayout cname = holderView.findViewById(R.id.updateProfileCollegeName);
            final TextInputLayout idnum = holderView.findViewById(R.id.updateProfileIDNumber);
            final TextInputLayout email = holderView.findViewById(R.id.updateProfileEmail);

            Button update = holderView.findViewById(R.id.updateProfile);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    Objects.requireNonNull(name.getEditText()).setText(user.getfName());
                    Objects.requireNonNull(cname.getEditText()).setText(user.getcName());
                    Objects.requireNonNull(idnum.getEditText()).setText(user.getIDNumber());
                    Objects.requireNonNull(email.getEditText()).setText(user.getEmailID());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            update.setOnClickListener(v1 -> {

                Map<String, Object> map = new HashMap<>();
                map.put("fName", Objects.requireNonNull(name.getEditText()).getText().toString().trim());
                map.put("cName", Objects.requireNonNull(cname.getEditText()).getText().toString().trim());
                map.put("idnumber", Objects.requireNonNull(idnum.getEditText()).getText().toString().trim());
                map.put("emailID", Objects.requireNonNull(email.getEditText()).getText().toString().trim());

                FirebaseDatabase.getInstance().getReference("Users")
                        .child(userID)
                        .updateChildren(map)
                        .addOnCompleteListener(task -> {
                            Toast.makeText(getContext(), "Updated Successfully!!!", Toast.LENGTH_SHORT).show();
                            dialogOne.dismiss();
                        });
            });

            dialogOne.show();
        });

        return view;
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = Objects.requireNonNull(this.getActivity()).getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setTitle("Profile Photo Upload");
        progressDialog.setMessage("Uploading your Image...");
        progressDialog.show();

        if (imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(task -> {
                if (! task.isSuccessful()){
                    throw Objects.requireNonNull(task.getException());
                }
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    assert downloadUri != null;
                    String mUri = downloadUri.toString();

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("profilePhoto", mUri);
                    reference.updateChildren(map);
                    progressDialog.dismiss();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Failed to upload image..!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }else {
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == -1
                && data != null && data.getData() != null){
            imageUri = data.getData();
            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
            }else {
                uploadImage();
            }
        }
    }
}