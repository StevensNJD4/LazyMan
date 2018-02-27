#!/bin/ksh

ZIP=/usr/bin/zip

function create {
  TARGET=$1.zip
#TARGET=plugin.video.lazyman.nhl.tv.zip 
  rm $1.zip
  $ZIP -r $1.zip $1
}

create plugin.video.lazyman.nhl.tv
create repository.lazyman
