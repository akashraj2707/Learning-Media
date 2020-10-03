package com.example.instaclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.List;

public class Postactivity extends AppCompatActivity {
    private ImageView close;
    private String imageUrl;
    private ImageView image_added;
    private TextView post;
    SocialAutoCompleteTextView description;
    private Uri imageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postactivity);

        close=findViewById(R.id.close);
        image_added=findViewById(R.id.image_added);
        post=findViewById(R.id.post);
        description=findViewById(R.id.description);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Postactivity.this,home.class));
                finish();
            }
        });

        //uploading image
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });
        CropImage.activity().start(Postactivity.this);

    }

    private void upload() {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        if(imageUri!=null){
            final StorageReference filePath= FirebaseStorage.getInstance().getReference("Posts")
                    .child(System.currentTimeMillis()+"."+getFileExtension(imageUri));

            StorageTask uploadTask=filePath.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri=task.getResult();
                    imageUrl=downloadUri.toString();

                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts");
                    String postId=ref.push().getKey();
                    HashMap<String,Object> map=new HashMap<>();
                    map.put("postId",postId);
                    map.put("imageUrl",imageUrl);
                    map.put("description",description.getText().toString());
                    map.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ref.child(postId).setValue(map);

                    DatabaseReference mHashTagRef=FirebaseDatabase.getInstance().getReference().child("HashTags");
                    List<String> hashTags=description.getHashtags();
                    if(!hashTags.isEmpty()){
                        for(String tag:hashTags){
                            map.clear();
                            map.put("tag",tag.toLowerCase());
                            map.put("postId",postId);
                            mHashTagRef.child(tag.toLowerCase()).child(postId).setValue(map);
                        }
                    }
                    pd.dismiss();
                    startActivity(new Intent(Postactivity.this,home.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Postactivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Toast.makeText(Postactivity.this,"No image was selected",Toast.LENGTH_LONG).show();
        }
    }

    private String getFileExtension(Uri Uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(Uri));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();
            image_added.setImageURI(imageUri);
        }else{
            Toast.makeText(Postactivity.this,"cancelled,Try again",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Postactivity.this,home.class));
            finish();
        }
    }
}