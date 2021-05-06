package com.zee.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.zee.log.ZException;
import com.zee.log.ZLog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FileUtil {
    private final static byte[] gSyncCode = new byte[0];

    public static String readFile(String filename) throws ZException {
        FileInputStream fis = null;
        File file = new File(filename);
        if (!file.exists()) {
            return "";
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, length);
            }
            fis.close();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(bos.toByteArray());
    }

    public static boolean writeFileStr(String filePath, String str) throws ZException {
        return writeFileAdd(filePath, str);
    }

    public static boolean writeFileAdd(String filePath, String str) throws ZException {
        return writeFileAdd(filePath, str, true);
    }

    public static boolean writeFileAdd(String filePath, String str, boolean append) throws ZException {
        try {
            File f = new File(filePath);
            File parentFile = f.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (!f.exists()) {
                f.createNewFile();
            }
            BufferedWriter output = new BufferedWriter(new FileWriter(f, append));
            output.write(str);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<String> readFileByLines(String fileName) {
        List<String> materialList = new ArrayList<>();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                materialList.add(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return materialList;
    }


    public static void write(Context context, String fileName, String content) {
        if (content == null) {
            content = "";
        }

        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(content.getBytes());

            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String read(Context context, String fileName) {
        try {
            FileInputStream in = context.openFileInput(fileName);
            return readInStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String readInStream(InputStream inStream) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }

            outStream.close();
            inStream.close();
            return outStream.toString();
        } catch (IOException e) {
            Log.i("FileTest", e.getMessage());
        }
        return null;
    }


    public static List<String> listPathFilesNames(String root) {
        List<String> allDir = new ArrayList<>();
        SecurityManager checker = new SecurityManager();
        File path = new File(root);
        if (path != null && path.exists()) {
            checker.checkRead(root);
            File[] files = listSortedFiles(path);
            if (files != null) {
                for (File f : files) {
                    if (f.isFile()) {
                        allDir.add(root + f.getName());
                    }
                }
            }
        }
        return allDir;
    }

    public static String getAssetsString(String fileName) {
        String result = "";
        InputStream in = null;
        try {
            in = ZLibrary.getInstance().getApplicationContext().getAssets().open(fileName);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);

            result = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String getSDString(String fileName) {
        String sdpath = Environment.getExternalStorageDirectory().getPath();
        return getString(sdpath + "/" + fileName);
    }

    public static String getString(String fileName) {

        String result = "";
        InputStream in = null;
        try {
            File file = new File(fileName);
            in = new FileInputStream(file);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            result = new String(buffer, "UTF-8");
        } catch (Exception e) {

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return result;
    }

    public static File[] listSortedFiles(File dirFile) {
        File[] files = dirFile.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }
        FileWrapper[] fileWrappers = new FileWrapper[files.length];
        for (int i = 0; i < files.length; i++) {
            fileWrappers[i] = new FileWrapper(files[i]);
        }

        Arrays.sort(fileWrappers);

        File[] sortedFiles = new File[files.length];
        for (int i = 0; i < files.length; i++) {
            sortedFiles[i] = fileWrappers[i].getFile();
        }

        return sortedFiles;
    }

    public static String getAssetsUTFFile(String fileName) {
        String result = "";
        InputStream in = null;
        try {
            in = ZLibrary.getInstance().getApplicationContext().getAssets().open(fileName);
            //获取文字字数
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            //设置编码
            result = new String(buffer, "UTF-8");
        } catch (Exception e) {
            ZLog.exception(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    ZLog.exception(e1);
                }
            }
        }
        return result;
    }

    public static boolean deleteFile(String path) {

        synchronized (gSyncCode) {
            if (TextUtils.isEmpty(path)) {
                return true;
            }

            File file = new File(path);
            if (!file.exists()) {
                return true;
            }
            if (file.isFile()) {
                return file.delete();
            }
            if (!file.isDirectory()) {
                return false;
            }
            File[] filesList = file.listFiles();

            if (filesList != null) {
                for (File f : filesList) {
                    if (f.isFile()) {
                        f.delete();
                    } else if (f.isDirectory()) {
                        deleteFile(f.getAbsolutePath());
                    }
                }
            }

            return file.delete();
        }
    }

    static boolean deleteFile(File paramFile) {
        return deleteFile(paramFile.getAbsolutePath());
    }

    static class FileWrapper implements Comparable {
        /**
         * File
         */
        private File file;

        public FileWrapper(File file) {
            this.file = file;
        }

        @Override
        public int compareTo(Object obj) {

            FileWrapper castObj = (FileWrapper) obj;

            if (this.file.getName().compareTo(castObj.getFile().getName()) > 0) {
                return 1;
            } else if (this.file.getName().compareTo(castObj.getFile().getName()) < 0) {
                return -1;
            } else {
                return 0;
            }
        }

        public File getFile() {
            return this.file;
        }
    }

    //获得sd卡地址
    public String getSDcardPath() {
        return Comment.sdCardPath;
    }

}
