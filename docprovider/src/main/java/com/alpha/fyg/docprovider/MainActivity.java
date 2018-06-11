package com.alpha.fyg.docprovider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity {
    LinearLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parent = findViewById(R.id.parent);
//        LoadingBar.make(parent).show();
    }

    public void btnGetProvider(View view) {
//        LoadingBar.cancel(parent);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            startActivityForResult(intent, 42);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (resultCode == RESULT_OK) {
            Uri treeUri = resultData.getData();
            Log.e("MainActivity", "treeUri:" + treeUri);
            getUri(treeUri, "/test/test.txt");

            DocumentFile pickedDir = DocumentFile.fromTreeUri(this, treeUri);

            for (DocumentFile file : pickedDir.listFiles()) {
                if (file.isDirectory()) {
                    for (DocumentFile file1 : file.listFiles()) {
                        if ("test.txt".equals(file1.getName())) {
                            Log.e("MainActivity", "uri:" + file1.getUri());
                        }
                    }
                } else {
                    if ("test.txt".equals(file.getName())) {
                        Log.e("MainActivity", "uri:" + file.getUri());
                    }
                }
            }
//            // List all existing files inside picked directory
//            for (DocumentFile file : pickedDir.listFiles()) {
//                Log.e("MainActivity", "Found file " + file.getName() + " with size " + file.length());
//                Log.e("MainActivity", "file.getUri():" + file.getUri());
//                if (file.getName().equals("test")) {
//                    Log.e("MainActivity", "file.canWrite():" + file.canWrite());
//                    if (file.canWrite()) {
//                        file.delete();
//                    }
//                }
//            }

//            createFile(treeUri);


        }
    }

    private Uri getUri(Uri treeUri, String filepath) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getContentResolver().takePersistableUriPermission(treeUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION |
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                uri = DocumentsContract.buildDocumentUriUsingTree(treeUri, "://test/test.txt");
                Uri uri1 = DocumentsContract.buildDocumentUriUsingTree(treeUri, DocumentsContract.getTreeDocumentId(treeUri) + "/test/test.txt");
                DocumentFile file = DocumentFile.fromSingleUri(MainActivity.this, uri1);
                boolean isDelete = file.delete();
                Log.e("MainActivity", "isDelete:" + isDelete);

                //DocumentsContract.getTreeDocumentId(uri)
//                Log.e("MainActivity1", "uri:" + uri);
                Log.e("MainActivity", "uri1:" + uri1);

            }
        }

        return uri;
    }

    /**
     * @param treeUri
     */
    private void createFile(Uri treeUri) {
        DocumentFile pickedDir = DocumentFile.fromTreeUri(this, treeUri);
        // List all existing files inside picked directory
        for (DocumentFile file : pickedDir.listFiles()) {
            Log.e("MainActivity", "Found file " + file.getName() + " with size " + file.length());
        }
        // Create a new file and write into it
        DocumentFile newFile = pickedDir.createFile("text/plain", "My Novel");
        OutputStream out = null;
        try {
            out = getContentResolver().openOutputStream(newFile.getUri());
//            getContentResolver().openInputStream()
            out.write("A long time ago...".getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
