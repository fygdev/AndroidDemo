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

import java.io.File;
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
            DocumentFile pickedDir = DocumentFile.fromTreeUri(this, treeUri);
//            pickedDir.createDirectory("abc");

            Uri testUri = getUri(treeUri, "/test/test.txt");
            Log.e("MainActivity", "testUri:" + testUri);
            DocumentFile documentFile = DocumentFile.fromSingleUri(MainActivity.this, testUri);
            Uri targetUri = getUri(treeUri, "/abc");

            try {
//                    DocumentsContract.moveDocument(getContentResolver(), testUri, documentFile.getParentFile().getUri(), targetUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    DocumentsContract.copyDocument(getContentResolver(), testUri, targetUri);
                } else {
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

//            OutputStream out = null;
//            try {
//                out = getContentResolver().openOutputStream(documentFile.getUri());
////            getContentResolver().openInputStream()
//                out.write("A long time ago...".getBytes());
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (out != null) {
//                    try {
//                        out.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//

        }
    }

    private Uri getUri(Uri treeUri, String filepath) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getContentResolver().takePersistableUriPermission(treeUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION |
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                uri = DocumentsContract.buildDocumentUriUsingTree(treeUri, DocumentsContract.getTreeDocumentId(treeUri) + filepath);
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
