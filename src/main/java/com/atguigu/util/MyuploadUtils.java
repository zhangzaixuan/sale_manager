package com.atguigu.util;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class MyuploadUtils {

	public static List<String> upload_image(MultipartFile[] multiPartFiles) {
		List<String> list_image=new ArrayList<String>();
		for (int i = 0; i < multiPartFiles.length; i++) {
			if (!multiPartFiles[i].isEmpty()) {
				
				String image_name = UUID.randomUUID().toString()+multiPartFiles[i].getOriginalFilename();
				try {
					multiPartFiles[i].transferTo(new File(MyPorpertiesUtil.getMypath("upload.properties","windows_image_path"),image_name));
					list_image.add(image_name);
				} catch (IllegalStateException | IOException e) {
					
					e.printStackTrace();
					
				}
			}
			
		}
		
		return list_image;
	}
	

}
