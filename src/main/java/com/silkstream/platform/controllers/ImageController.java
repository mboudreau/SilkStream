package com.silkstream.platform.controllers;

import magick.ImageInfo;
import magick.MagickImage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.awt.*;

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

			//BufferedImage image = ImageIO.read(resource.getFile());
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			ImageIO.write(image, "jpg", baos);
//			bytes = baos.toByteArray();
//			baos.close();

			ImageInfo origInfo = new ImageInfo(resource.getFile().getAbsolutePath()); //load image info
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
				Dimension d = getScaledDimension(magickImage.getDimension(), new Dimension(width, height));
				magickImage = magickImage.scaleImage(d.width, d.height); //to Scale image
			}

			//magickImage.setFileName(absNewFilePath); //give new location
			//magickImage.writeImage(origInfo); //save
			bytes = magickImage.imageToBlob(origInfo);

		} catch (Exception e) {

		}
		return bytes;
	}

	protected Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

		int original_width = imgSize.width;
		int original_height = imgSize.height;
		int bound_width = boundary.width;
		int bound_height = boundary.height;
		int new_width = original_width;
		int new_height = original_height;

		// first check if we need to scale width
		if (original_width > bound_width) {
			//scale width to fit
			new_width = bound_width;
			//scale height to maintain aspect ratio
			new_height = (new_width * original_height) / original_width;
		}

		// then check if we need to scale even with the new height
		if (new_height > bound_height) {
			//scale height to fit instead
			new_height = bound_height;
			//scale width to maintain aspect ratio
			new_width = (new_height * original_width) / original_height;
		}

		return new Dimension(new_width, new_height);
	}
}
