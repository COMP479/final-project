# COMP 479 - Final Project

## Description:
This project crawls a set of Concordia webpages to determine general sentiment towards certain subjects.

Crawls pages using: https://github.com/yasserg/crawler4j

Parses pages using: 

## Project Structure:
### Scraper:
Scraper can be found in `src/main/java`

It is configured and run by ScraperController

The scraping is done in MyCrawler

Scraper stores html pages in folders with titles taken from their root pages inside `src/html`

The scraper library stores certain information that we don't use in `src/data`

### Indexer
Indexer can be found in `src/main/java`

It uses the following folders in `src`: `blocks`, `index`, `stats`

The classes inside Models were modified to be used with text instead of xml

The Preprocessor inside IndexBuilder was modified to process html files using BoilerPipe

IMPORTANT: Before the Indexer can be run, make sure you have run the scraper to generate the appropriate html files

## Set up:

### Dependencies
Java 1.8

### For development
Fork project

git clone or download the repo

cd into project

git remote add main original_project_url

### To open in Eclipse:
Open eclipse

`File -> Import -> Maven -> Existing Maven Project`

Choose the cloned/downloaded repo
