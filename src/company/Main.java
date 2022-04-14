package company;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

	//сохранение
	private static void saveGame(String path, GameProgress progress) {

		try (FileOutputStream fos = new FileOutputStream(path);
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {

			oos.writeObject(progress);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}


	//архивация
	private static void zipFiles(String pathToArchive, String pathToSave) {

		//фильтр
		FileFilter filter = pathname -> {
			if (pathname.getName().endsWith(".dat")) {
				return true;
			}
			return false;
		};

		FileInputStream fis = null;

		//вход в директорию
		File box = new File(pathToSave);

		try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(pathToArchive))) {

			//обход директории
			for (File item : box.listFiles(filter)) {
				if (box.isDirectory()) {
					String name = item.getName();
					String path = item.getPath();

					fis = new FileInputStream(path);

					ZipEntry zen = new ZipEntry(name);
					zout.putNextEntry(zen);
					byte[] buffer = new byte[fis.available()];
					fis.read(buffer);
					zout.write(buffer);

					try {
						fis.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}

					item.delete();
				}
			}

			zout.closeEntry();

		} catch (IOException | RuntimeException ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		GameProgress first = new GameProgress(100, 1, 1, 10.0);
		GameProgress second = new GameProgress(70, 5, 3, 320.5);
		GameProgress third = new GameProgress(30, 15, 10, 723.6);

		saveGame("C:\\Games\\savegames\\save1.dat", first);
		saveGame("C:\\Games\\savegames\\save2.dat", second);
		saveGame("C:\\Games\\savegames\\save3.dat", third);

		zipFiles("C:\\Games\\savegames\\saves.zip",
				"C:\\Games\\savegames");

	}
}
