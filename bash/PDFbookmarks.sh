#!/bin/bash

java -jar PDFbookmarks.jar

ls *.pdf > filenames.txt

gs -sDEVICE=pdfwrite -dBATCH -dNOPAUSE -dAutoRotatePages=/None -sOutputFile=bookmarks.pdf index.info -f @filenames.txt

rm -f index.info

rm -f filenames.txt

