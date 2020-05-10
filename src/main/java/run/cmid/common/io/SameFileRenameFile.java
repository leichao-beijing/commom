package run.cmid.common.io;

import java.io.File;

/**
 * 针对目录内相同的文件进行重命名
 */
public class SameFileRenameFile {

    private File file;
    private SameFileRename sameFileRename;

    public SameFileRenameFile(File file) {
        this.file = file;
        this.sameFileRename = new SameFileRename();
        readFile();
    }

    private void readFile() {
        File[] files = file.listFiles();
        for (File file : files) {
            sameFileRename.add(file.getName());
        }
    }

    /**
     * 获取重命名后的文件名称
     */
    public String renameName(String fileName) {
        return sameFileRename.renameName(fileName);
    }
}
