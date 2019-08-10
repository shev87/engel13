package autocomponent;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFileManager {
    private final File zipFileSource;
    private final String passwordAccess = "PasswordAccess\">\r\n<Value>false</Value>";
    private final String passwordTrue = "PasswordAccess\">\r\n<Value>true</Value>";

    public ZipFileManager(File file) {
        this.zipFileSource = file;
    }

    public String changeFileToExistingZip() throws Exception {
        String machineId = "";
        // get a temp file
        File tempFile = File.createTempFile(zipFileSource.getName(), null);
        // delete it, otherwise you cannot rename your existing zip to it.
        tempFile.delete();

        boolean renameOk=zipFileSource.renameTo(tempFile);
        if (!renameOk)
        {
            throw new RuntimeException("could not rename the file "+ zipFileSource.getAbsolutePath()+" to "+tempFile.getAbsolutePath());
        }
        byte[] buf = new byte[1024];

        ZipInputStream zin = new ZipInputStream(new FileInputStream(tempFile));
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileSource));

        ZipEntry entry = zin.getNextEntry();
        while (entry != null) {
            String name = entry.getName();

            if (name.equals("immdata/MachineDisk.xml")) {
                out.putNextEntry(new ZipEntry("immdata/MachineDisk.xml"));
                int len;
                while ((len = (zin.read(buf))) > 0) {
                    String s = new String(buf);
                    if (s.contains("Machine_ID=\"")){
                        String tmp = "";

                        Pattern pattern = Pattern.compile("ID=\"\\d*\"");
                        Matcher matcher = pattern.matcher(s);
                        while(matcher.find())
                            tmp = matcher.group();
                       Pattern p = Pattern.compile("\\d+");
                        Matcher m = p.matcher(tmp);
                        while(m.find())
                            machineId = m.group();

                    }
                    if (s.contains(passwordAccess)) {
                        buf = s.replaceAll(passwordAccess, passwordTrue).getBytes();
                    }
                    out.write(buf, 0, (len < buf.length) ? len : buf.length);
                }
                out.closeEntry();
            } else {
                // Add ZIP entry to output stream.
                out.putNextEntry(new ZipEntry(name));
                // Transfer bytes from the ZIP file to the output file
                int len;
                while ((len = zin.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
            entry = zin.getNextEntry();
        }
        // Close the streams
        zin.close();
        // Complete the ZIP file
        out.close();
        tempFile.delete();
        return machineId;
    }

    public void copyMachineData() throws IOException{
       if (Files.notExists(Paths.get("C:/EngelData")))
            Files.createDirectory(Paths.get("C:/EngelData"));
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        String data = "C:/EngelData/" + dateFormat.format(date);
        Files.createDirectory(Paths.get(data));
        Files.copy(zipFileSource.toPath(), Paths.get(data + "/" + zipFileSource.getName()), StandardCopyOption.REPLACE_EXISTING);
    }
}
