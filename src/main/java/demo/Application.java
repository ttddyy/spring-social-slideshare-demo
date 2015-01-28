package demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.social.slideshare.api.SlideShare;
import org.springframework.social.slideshare.api.SlideshowOperations;
import org.springframework.social.slideshare.api.domain.GetSlideshowsResponse;
import org.springframework.social.slideshare.api.domain.PrivacySetting;
import org.springframework.social.slideshare.api.domain.SearchSlideshowsResponse;
import org.springframework.social.slideshare.api.domain.Slideshow;
import org.springframework.social.slideshare.api.impl.SlideShareTemplate;
import org.springframework.util.Assert;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Application {

	@Value ( "${slideshare.apikey}" )
	private String apiKey;

	@Value ("${slideshare.secret}")
	private String sharedSecret;

	@Value ( "${slideshare.username}" )
	private String username;

	@Value ("${slideshare.password}")
	private String password;

	@Value ("${slideshare.slideFilePath}")
	private String slideFilePath;

	private static final int FETCH_SIZE = 3;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner init() {
		return args -> {

			Assert.hasText(this.apiKey, "SLIDESHARE_APIKEY is required");
			Assert.hasText(this.sharedSecret, "SLIDESHARE_SECRET is required");
			Assert.hasText(this.username, "SLIDESHARE_USERNAME is required");
			Assert.hasText(this.password, "SLIDESHARE_PASSWORD is required");

			System.out.println("===============================");
			System.out.println("= RUNNING DEMO APPLICATION    =");
			System.out.println("===============================");

			System.out.println("APIKEY: " + this.apiKey);
			System.out.println("USERNAME: " + this.username);
			System.out.println();

			SlideShare slideshare = new SlideShareTemplate(apiKey, sharedSecret);
			SlideshowOperations slideshow = slideshare.slideshowOperations();

			getSingleSlideshow(slideshow);
			getMultipleSlideshows(slideshow);
			search(slideshow);

			// upload, edit, delete   (upload needs extra permission on your slideshare dev account)
			String uplodedId;
			uplodedId = uploadLocalFile(slideshow);
			edit(slideshow, uplodedId);
			delete(slideshow, uplodedId);
		};
	}

	private void getSingleSlideshow(SlideshowOperations slideshow) {

		System.out.println("=======================================");
		System.out.println("= Retrieving a slideshow by ID or URL =");
		System.out.println("=======================================");

		Slideshow show;
		// by ID
		// show = slideshow.getSlideshowById("41084028");  // "Booting up Spring Social"

		// by URL
		show = slideshow.getSlideshowByUrl("http://www.slideshare.net/SpringCentral/booting-up-spring-social");
		System.out.println("ID: " + show.getId());
		System.out.println("URL: " + show.getUrl());
		System.out.println("TITLE: " + show.getTitle());
		System.out.println("DESCRIPTION: " + show.getDescription().substring(0, 70) + "...");  // only display part
		System.out.println("URL: " + show.getUrl());
		System.out.println("EMBED: " + show.getEmbed());
		System.out.println("DOWNLOAD URL: " + show.getDownloadUrl());
		System.out.println("USERNAME: " + show.getUsername());
		System.out.println("=======================================");
		System.out.println();
	}

	private void getMultipleSlideshows(SlideshowOperations slideshow) {

		System.out.println("=========================================================");
		System.out.println("= Retrieving multiple slideshows by User, Tag, or Group =");
		System.out.println("=========================================================");

		GetSlideshowsResponse response;
		List<Slideshow> shows;

		System.out.println("=== By User ===");
		response = slideshow.getSlideshowsByUser("SpringCentral", FETCH_SIZE);
		shows = response.getSlideshows();

		System.out.println("REQUEST TYPE: " + response.getRequestType().name());
		System.out.println("TOTAL COUNT: " + response.getCount());
		System.out.println("FIRST SLIDESHOW ID: " + shows.get(0).getId());
		System.out.println("FIRST SLIDESHOW TITLE: " + shows.get(0).getTitle());
		System.out.println();


		System.out.println("=== By Tag ===");
		response = slideshow.getSlideshowsByTag("spring", FETCH_SIZE);
		shows = response.getSlideshows();

		System.out.println("REQUEST TYPE: " + response.getRequestType().name());
		System.out.println("TOTAL COUNT: " + response.getCount());
		System.out.println("FIRST SLIDESHOW ID: " + shows.get(0).getId());
		System.out.println("FIRST SLIDESHOW TITLE: " + shows.get(0).getTitle());
		System.out.println("=========================================================");
		System.out.println();
	}

	private void search(SlideshowOperations slideshow) {

		System.out.println("=====================");
		System.out.println("= Search slideshows =");
		System.out.println("=====================");


		SearchSlideshowsResponse response;
		List<Slideshow> shows;
		int page = 3;
		int itemsPerPage = 20;

		// response = slideshow.searchSlideshows("spring");  // use default pagination params
		response = slideshow.searchSlideshows("spring", page, itemsPerPage);  // paginated access
		shows = response.getSlideshows();

		response.getMetaInfo().getNumResults();
		response.getMetaInfo().getTotalResults();

		System.out.println("SEARCH QUERY: " + response.getQuery());
		System.out.println("NUM OF RESULTS: " + response.getNumResults());
		System.out.println("TOTAL RESULTS: " + response.getTotalResults());
		System.out.println("FIRST SLIDESHOW ID: " + shows.get(0).getId());
		System.out.println("FIRST SLIDESHOW TITLE: " + shows.get(0).getTitle());
		System.out.println("=====================");
		System.out.println();
	}

	private String uploadLocalFile(SlideshowOperations slideshow) {

		Assert.hasText(this.slideFilePath, "SLIDESHARE_SLIDEFILEPATH is required to upload a local presentation file.");

		System.out.println("====================================");
		System.out.println("= Upload a local presentation file =");
		System.out.println("====================================");

		File file = new File(this.slideFilePath);
		String uploadedId = slideshow
				.uploadSlideshowFromFile(username, password, file, "My Title", "My Description");

		// slides can be uploaded from spring's Resource, file content, or Url.
		//   slideshow.uploadSlideshowResource()
		//   slideshow.uploadSlideshowFromContent()
		//   slideshow.uploadSlideshowFromUrl()

		System.out.println("UPLOADED ID: " + uploadedId);
		System.out.println("UPLOADED USER ACCOUNT: " + this.username);
		System.out.println("====================================");
		System.out.println();

		return uploadedId;
	}

	private void edit(SlideshowOperations slideshow, String id) {

		System.out.println("==================");
		System.out.println("= Edit slideshow =");
		System.out.println("==================");

		List<String> tags = Arrays.asList("spring", "social");
		PrivacySetting privacy = new PrivacySetting();
		privacy.setMakeSlideshowPrivate(false);  // make slideshow public

		String updatedId = slideshow.editSlideshow(username, password, id, "Modified Title", "Modified Desc", tags, privacy);
		System.out.println("MODIFIED SLIDESHOW ID: " + updatedId);
		System.out.println("==================");
		System.out.println();
	}

	private void delete(SlideshowOperations slideshow, String slideshowId) {

		System.out.println("====================");
		System.out.println("= Delete slideshow =");
		System.out.println("====================");

		String deletedId = slideshow.deleteSlideshow(username, password, slideshowId);
		System.out.println("DELETED SLIDESHOW ID: " + deletedId);
		System.out.println("====================");
		System.out.println();
	}

}
