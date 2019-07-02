package main.java;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class Controller {
    private View view;
    private File fileOriginal;
    private File fileOutput;

    public File getFileOriginal() {
        return fileOriginal;
    }

    public File getFileOutput() {
        return fileOutput;
    }

    public static void main(String[] args) {
        View view = new View();
        Controller controller = new Controller(view);
        view.setController(controller);
        view.init();
        controller.init();
    }

    public void exit(){
        int yes = JOptionPane.showConfirmDialog(view.getjPanel(), "Выйти из программы?", "Engel13", JOptionPane.YES_NO_OPTION);
        if (yes == 0) System.exit(0);
    }

    public void init(){

    }

    public void loadMachineData(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("c://"));
        //fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.getName().toLowerCase().endsWith(".zip")) return true;
                if (f.isDirectory()) return true;
                return false;
            }

            @Override
            public String getDescription() {
                return "ZIP";
            }
        });
        fileChooser.showOpenDialog(view.getjPanel());
        try {
            fileOriginal = fileChooser.getSelectedFile();
            view.createLabelButtonLoad(fileOriginal.getAbsolutePath());
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(view.getjPanel(), "Выбери файл, товарищ инженер!", "Предупреждение", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void showAboutMessage(){
        JOptionPane.showMessageDialog(view.getjPanel(), "Engel13\nверсия 0.01", "Информация о программе", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showHowToWork(){
        JOptionPane.showMessageDialog(view.getjPanel(), Helper.description, "Engel13", JOptionPane.INFORMATION_MESSAGE);

    }

    public void showStartWindow(){
        String s = view.getButtonGroup().getSelection().getActionCommand();
        String message = "";
        if (s.equals("password")) message = "доступ по паролю";
        else if (s.equals("card")) message = "доступ по карте";
        int yes = JOptionPane.showConfirmDialog(view.getjPanel(), "Вы выбрали: " + message + "\nНачать преобразование данных?", "Engel13", JOptionPane.YES_NO_OPTION);
        if (yes == 0){
            try {
                if (fileOriginal == null) throw new NullPointerException();
                Thread.sleep(1500);
                JOptionPane.showMessageDialog(view.getjPanel(), "Преобразование данных успешно завершено!", "Engel13", JOptionPane.INFORMATION_MESSAGE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                JOptionPane.showMessageDialog(view.getjPanel(), "Выбери файл, товарищ инженер!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public Controller(View view) {
        this.view = view;
    }
}
