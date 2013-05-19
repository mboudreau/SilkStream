package com.silkstream.platform.controllers;

import magick.ImageInfo;
import magick.MagickImage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

@Controller
@RequestMapping(value = "/api/image", produces = "application/json")
public class ImageController extends BasicController {

	public ImageController(){
		System.setProperty("jmagick.systemclassloader", "no");
//		try {
//		Magick.class.getClassLoader() .loadClass("magick.MagickLoader").newInstance();
//		}catch(Exception e) {
//
//		}
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@RequestMapping(value = "api/**", method = RequestMethod.GET)
	public ModelAndView wrongApi(ModelAndView model) {
		model.addObject("Error", "Wrong api called");
		model.setViewName("error");
		return model;
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView accessDenied(ModelAndView model) {
		//model.addObject("ApplicationException", "Wrong api called");
		model.setViewName("error");
		return model;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.GET, value = "{id}", produces = "application/octet-stream")
	@ResponseBody
	public byte[] getImage(@PathVariable String id, @RequestParam(value="width", required = false) int width, @RequestParam(value="height", required = false) int height,
	                    @RequestParam(value="zoom", required = false) float zoom, @RequestParam(value="pixelRatio", required = false) float pixelRatio,
	                    @RequestParam(value="crop", required = false) int[] crop) {
		Rectangle cropRect = null;
		if(crop != null) {
			cropRect = new Rectangle(crop[0], crop[1], crop[2], crop[4]);
		}

		ClassPathResource resource = new ClassPathResource("images/" + id + ".jpg");
		if (!resource.exists()) {
			throw new Error("ID is not valid.");
		}
		byte[] bytes = null;
		try {

			BufferedImage image = ImageIO.read(resource.getFile());
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			ImageIO.write(image, "jpg", baos);
//			bytes = baos.toByteArray();
//			baos.close();

			ImageInfo origInfo = new ImageInfo(resource.getPath()); //load image info
			if(pixelRatio != 0) {
				origInfo.setDensity((96 * pixelRatio) + ""); // set DPI
			}
			MagickImage magickImage = new MagickImage(origInfo); //load image
			if(cropRect != null) {
				magickImage = magickImage.cropImage(cropRect);
			}
			if(width != 0 || height != 0) {
				width = width == 0?magickImage.getDimension().width:width;
				height = height == 0?magickImage.getDimension().height:height;
				magickImage = magickImage.scaleImage(width, height); //to Scale image
			}

			//magickImage.setFileName(absNewFilePath); //give new location
			//magickImage.writeImage(origInfo); //save
			bytes = magickImage.imageToBlob(origInfo);

		} catch (Exception e) {

		}
		return bytes;
	}
}
