AMFA
=======

AMFA stands for Ad Mediation for Android.

This project was designed to give more power to the developers to control who and how ads work in their app.  If you aren't 100% committed to a single ad mediator and may want to switch in the future then you'd want to control that as well as being able to configure older versions of your app to point to the new mediator.  

Current Capabilities
--------------------
* Control ad network distribution of available ad types
* Control how often ads are shown
* Retry logic if an ad provider fails to provide you an ad
* Provide specific logic for specific ad ids (If you display two different types of ad on your app you can configure each one)
* Dynamically change these preferences by providing a url that has updated logic

Using AMFA in your app
----------------------

You need to first add the AMFA code base to your app. The best way to do this is to checkout the project and package a jar file from the classes provided.

The jar packing command to be executed from the project's root directory.
'''.sh
jar cf amfa-core.jar src/*
'''

This can also be done by setting up the project to make contributions and marking it as a library and referencing it in your application project.

You then need to add your ad provider's SDKs (see list below for supported ad providers) and add anything they require in terms of permissions or activities to your AndroidManifest.xml. This data can be found on each company's help/getting started for Android documentation.

Configuring AMFA in your app
----------------------------

AMFA configuration is driven by Android's [metadata].

A single line in the metadata goes into AndroidManifest.xml and looks like the following:

```xml
<meta-data android:name="Name of AMFA property" android:value="Value of AMFA property" />
```

The following properties are available for configuration:

**provider_strategy**

How to select the next ad provider to query for an ad. Valid values are round_robin, weighted, and priority

Default: round_robin
  * round_robin - Select Ad Providers one after the other
  * weighted - Select Ad Providers based on a weight (see WeightedProviderSelectStrategy class until javadocs can be generated and posted)
  * priority - Select Ad Providers based on priority (see PriorityProviderSelectStrategy class until javadocs can be generated and posted)
  
**provider_value**

A csv of which ad providers should be used to deliver ads 

Default: all available providers in code base

**max_ad_requests**

How many times your app should attempt an ad request before stopping 

Default: 1 (This means do not attempt rerequest on failure)

**show_ad_percent**

How often should a user receive an ad on a scale of 0-100 

Default: 100 (This means always show an ad request)

**ad_ids**

Allows a developer to specifically configure ad requests for a specific location in their application

**dynamic_load_url**

A url the application can hit to download an updated version of AMFA's configuration. This will allow the developer to dynamically update all AMFA configurations for all apps and not just new versions

**admob_ad_unit_id**

If you are using AdMob in your app this is the id that they provide to you to designate your specific ad unit. See more [admob_ad_unit_id] details.

**mm_apid**

If you are using [Millennial Media] in your app this is the apid that they provide you to designate your specific ad unit. See more [mm_apid] details.

**mopub_ad_unit_id**

If you are using [MoPub] in your app this is the id that they provide to you to designate your specific ad unit. See more [mopub_ad_unit_id] details.


Supported Ad Providers
----------------------
* [AdMob]
* [Millennial Media]
* [MoPub]


Contributing Features
---------------------

Contributions to the github project is always appreciated. To contribute please fork this repository and make your changes with pull requests and details. New ad provider support is always desired. If you implement your own ad provider for an SDK currently not available on the project please share it.

* Fork your project
* Checkout your code and use Eclipse or IntelliJ for modification
* Import the code as an Android Library
* Add in all provider's SDKs in the lib folder to ensure the project builds (They are not provided to prevent issues with versioning and any issues with distribution of other people's code).
* Make your code changes and commit the code with all IDE specific code left out
* Make a pull request to the main repository

FAQs
-----
* Where are your unit tests?
  * Before I determined to share this project I was performing all my unit tests as more e2e tests in the apps using my projects themselves. I will work towards adding unit tests specifically to this project going forward.

* Is this production ready?
  * As most answers in software engineering, it depends. You could argue this is not ready because it does not have any unit tests directly on the project (see above). The other thing is I see a potential issue where breaking apart an apk and modifying an AndroidManifest.xml file is easier than decompiling and modifying code. I am comfortable with this because I do Android as more of a hobby than anything.  You may not be. I plan on looking at adding a coding override in the future to allow a developer to always specify settings in code thus making it harder to change the configurations of the app by a malicious third party. 

* Should I avoid doing mediation via an ad company and use this instead?
  * No.  You can, but ad providers who do mediation have ways to do great reporting or optimization on AdServing.  The ideal setup would probably be a priority strategy that lists the main ad provider who handles mediation for you first and then your other ad providers next.  This would ensure if something was to happen with the first ad provider or you want to switch that you will continue serving ads, but you still get all the benefits of allowing another company handle mediation for you.


[metadata]:http://developer.android.com/guide/topics/manifest/meta-data-element.html
[AdMob]: https://www.google.com/ads/admob/
[admob_ad_unit_id]:https://support.google.com/admob/v2/answer/3016009
[Millennial Media]: http://www.mmedia.com
[mm_apid]: http://docs.millennialmedia.com/android-SDK/AndroidFAQ.html
[MoPub]: http://www.mopub.com/
[mopub_ad_unit_id]: https://dev.twitter.com/mopub/android/interstitial