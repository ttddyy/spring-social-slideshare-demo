
This is a demo application for [spring-social-slideshare](https://github.com/ttddyy/spring-social-slideshare) 

## Setup

You need to [apply for slideshare API key](http://www.slideshare.net/developers/applyforapi).

To upload a presentation file, [you also need to ask extra permission.](http://www.slideshare.net/developers/documentation#upload_slideshow)
 
> Note: This method requires extra permissions. If you want to upload a file using SlideShare API, please send an email to api@slideshare.com with your developer account username describing the use case.


## How to run

```
> mvn spring-boot:run
```

You have to specify:

- Slideshare API Key _(SLIDESHARE_APIKEY)_
- Slideshare Shared Secret _(SLIDESHARE_SECRET)_
- Slideshare Username _(SLIDESHARE_USERNAME)_
- Slideshare Password _(SLIDESHARE_PASSWORD)_
- a presentation file such as ppt file _(SLIDESHARE_SLIDEFILEPATH)_


In command line:

```
> SLIDESHARE_APIKEY=... SLIDESHARE_SECRET=...  SLIDESHARE_USERNAME=... SLIDESHARE_PASSWORD=... SLIDESHARE_SLIDEFILEPATH=... mvn spring-boot:run
```

Or, you can create a `application.[properties/yaml/yml]` file to override those properties.