# Facebook Scraper

## Setup
1. create a top-level "out_files" directory if there is not one already (there should already be one)
1. within "out_files" create two subdirectories "parsed" and "merged"
1. in src/main/resources, create a "local.properties" file based off of "local.default.properties".  (For FB Creds, see the FB Creds section below.)  Note that BOTH the default properties file AND the local.properties file you create will be read, with local.properties overriding any values it specifies.
1. in src/main/resources, create a "config.json" file based off of "example.config.json".  In this file, specify defaults as shown, and specify Page Info objects.  Page Infos require at minimum a Page Name, this is the FB page to be scraped.  If no other fields are provided, the app will use the defaults.  Can override default comments-per-message and messages-per-page as shown.  This is useful for tuning scraping.  FB limits requests not based on the request but on the response.  If too much data would be returned, it throws an error.  (When an error is received for a page, the program moves on to the next page.)  So decreasing how much is requested for a busy page can help that.  On the other hand, since we only make a request every 5 minutes (or whatever you override to in config.properties), grabbing more data per request is better if it doesn't error out.  Please note that ONLY config.json is read and NOT the example config.json.

### Explanation
msgs per page is x

comments per msg is y

pages per page is z


so the idea is this.  it goes to a page and says, "give me x posts for pagename (each post having no more than y messages).  have I paged through z pages for this pagename? no?  okay, give me the next x posts for pagename"

## Usage
### Scraper
To use the scraper, run the following sbt command:

    sbt scrape
    
There is a known bug where after all scrapings have been attempted it doesn't stop running, but if you see the log statement "DONE WITH ALL JOBS" in std out you know that execution has been completed.

Be advised you may have to jiggle around the settings in config.json for different pages to balance successfully scraping vs scraping "fast enough".

All files for a given day are put into the same folder in out_files.  This holds true even if you run the program multiple separate times on the same day.  If you would like separate folders for some reason (ie you don't want to overwrite old ones, or for organization), just take today's folder in out_files and rename it, no harm will occur, and a new one for today will be created.

### Parser
To use the parser, run the follwoing sbt command:

    sbt "parse [folder_name]"

i.e. if there is a folder out_files/raw_2016-08-03a with jsons inside it, run

    sbt "parse raw_2016-08-03a"
    
Error jsons in the folder will be ignored.
The parsed data will be collected in a csv in the "parsed" folder, named with a timestamp.  Run merge to merge.
### Merger
Merges all csv files found in the "parsed" folder into a single file, removing duplicates as well.

    sbt merge
    
This file will be found in the merged folder.  Old files are not overwritten, each new files is created with a new timestamp, but each time a file is created it is the total merge of ALL of the files in the parsed directory.  To not merge a parsed file, do not include it in the directory.

## Facebook Credentials

To obtain facebook developer credentials, complete the following steps:

1. Log in / sign up for facebook
1. Go to https://developers.facebook.com/
1. Click on "Register" in the upper right, and follow the process
1. Click on "My Apps" in the upper right and click on "Requests"
1. Click "View Your Apps" then "Create a New App" and "Skip This Step: basic setup"
1. Fill in a name and email for the new app
1. Once created, choose Dashboard from the left hand menu to view a screen with App ID and Secret.

## Notes on Implementation
Note that settings are found in Settings.scala