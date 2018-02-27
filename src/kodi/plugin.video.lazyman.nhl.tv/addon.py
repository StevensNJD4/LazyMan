import xbmcaddon, xbmcgui, xbmcplugin, xbmc
import sys
import os
import calendar, datetime, time
import utils
import ConfigParser
import urllib, json
import socket
from datetime import datetime
from urlparse import parse_qsl
from game import *
from highlights import *
#import player

addonUrl = sys.argv[0]
addonHandle = int(sys.argv[1])
addonId = "video.lazyman.nhl.tv"
addon = xbmcaddon.Addon(id = addonId)
addonPath = addon.getAddonInfo('path')
addonName = addon.getAddonInfo('name')
sanityChecked = False
iniFilePath = os.path.join(addonPath, 'resources', 'lazyman.ini')
config = ConfigParser.ConfigParser()
config.read(iniFilePath)

def games(date,provider): 
  remaining = GameBuilder.nhlTvRemaining if provider == "NHL.tv" else GameBuilder.mlbTvRemaining
  return GameBuilder.fromDate(config,date,remaining,provider)

def listgrouphighlights(provider,group):
  items = []
  for hg in filter(lambda x: x.title == group, get_highlights(config,provider)):
    for h in hg.highlights:
      label = "{0} ({1})".format(h.blurb,h.duration)
      listItem = xbmcgui.ListItem(label = label)
      listItem.setInfo( type="Video", infoLabels={ "Title": label } )
      #xbmc.log("Highlight URL [%s]" % (h.playbackUrl), xbmc.LOGNOTICE)
      url = '{0}?action=playhighlight&url={1}'.format(addonUrl,h.playbackUrl)
      items.append((url, listItem, True))

  ok = xbmcplugin.addDirectoryItems(addonHandle, items, len(items)) 
  xbmcplugin.endOfDirectory(addonHandle)

def listhighlights(provider):
  items = []
  for hg in get_highlights(config,provider):
    listItem = xbmcgui.ListItem(label = str(hg.title))
    listItem.setInfo( type="Video", infoLabels={ "Title": str(hg.title) } )
    url = '{0}?action=listgrouphighlights&group={1}&provider={2}'.format(addonUrl,hg.title,provider)
    items.append((url, listItem, True))

  ok = xbmcplugin.addDirectoryItems(addonHandle, items, len(items)) 
  xbmcplugin.endOfDirectory(addonHandle)

def listyears(provider):
  items = []
  for y in utils.years(provider):
    listItem = xbmcgui.ListItem(label = str(y))
    listItem.setInfo( type="Video", infoLabels={ "Title": str(y) } )
    url = '{0}?action=listmonths&year={1}&provider={2}'.format(addonUrl,y,provider)
    items.append((url, listItem, True))

  ok = xbmcplugin.addDirectoryItems(addonHandle, items, len(items)) 
  xbmcplugin.endOfDirectory(addonHandle)

def highlights(provider):
  items = []


def listmonths(year,provider):
  items = []
  for (mn,m) in utils.months(year):
    listItem = xbmcgui.ListItem(label = mn)
    listItem.setInfo( type="Video", infoLabels={ "Title": mn } )
    url = '{0}?action=listdays&year={1}&month={2}&provider={3}'.format(addonUrl,year,m,provider)
    items.append((url, listItem, True))

  ok = xbmcplugin.addDirectoryItems(addonHandle, items, len(items)) 
  xbmcplugin.endOfDirectory(addonHandle)

def listdays(year,month,provider):
  items = []
  for d in utils.days(year,month):
    listItem = xbmcgui.ListItem(label = str(d))
    listItem.setInfo( type="Video", infoLabels={ "Title": str(d) } )
    url = '{0}?action=listgames&year={1}&month={2}&day={3}&provider={4}'.format(addonUrl,year,month,d,provider)
    items.append((url, listItem, True))

  ok = xbmcplugin.addDirectoryItems(addonHandle, items, len(items)) 
  xbmcplugin.endOfDirectory(addonHandle)


def listproviders():
  items = []
  providers = config.get("LazyMan","Providers").split(",")
  for provider in providers:
    listItem = xbmcgui.ListItem(label = provider)
    listItem.setInfo( type="Video", infoLabels={ "Title": provider } )
    url = '{0}?action=listtodaysgames&provider={1}'.format(addonUrl,provider)
    items.append((url, listItem, True))
  ok = xbmcplugin.addDirectoryItems(addonHandle, items, len(items)) 
  xbmcplugin.endOfDirectory(addonHandle)

def listgames(date,provider,previous = False,highlights = False):
  items = []
  dategames = games(date,provider) 
  for g in dategames: 
    label = "%s vs. %s [%s]" % (g.awayFull,g.homeFull,g.remaining if g.remaining != "N/A" else utils.asCurrentTz(date,g.time))
    listItem = xbmcgui.ListItem(label = label)
    listItem.setInfo( type="Video", infoLabels={ "Title": label } )
    url = '{0}?action=feeds&game={1}&date={2}&provider={3}'.format(addonUrl,g.id,date,provider)
    items.append((url, listItem, True))
  if len(items) == 0:
    xbmcgui.Dialog().ok(addonName, "No games scheduled today")
    
  if highlights:
    listItem = xbmcgui.ListItem(label = "Highlights")
    listItem.setInfo( type="Video", infoLabels={ "Title": "Highlights" } )
    url = '{0}?action=listhighlights&provider={1}'.format(addonUrl,provider)
    items.append((url, listItem, True))

  if previous:
    listItem = xbmcgui.ListItem(label = "Previous")
    listItem.setInfo( type="Video", infoLabels={ "Title": "Previous" } )
    url = '{0}?action=listyears&provider={1}'.format(addonUrl,provider)
    items.append((url, listItem, True))
  ok = xbmcplugin.addDirectoryItems(addonHandle, items, len(items)) 
  xbmcplugin.endOfDirectory(addonHandle)
  xbmc.log("Added %d games" % len(items))

def listfeeds(game,date,provider):
  items = []
  for f in filter(lambda f: f.viewable(), game.feeds):
    label = str(f)
    listItem = xbmcgui.ListItem(label = label)
    listItem.setInfo( type="Video", infoLabels={ "Title": label } )
    url = '{0}?action=play&date={1}&feedId={2}&provider={3}'.format(addonUrl,date,f.mediaId,provider)
    items.append((url, listItem, False))

  ok = xbmcplugin.addDirectoryItems(addonHandle, items, len(items)) 
  xbmcplugin.endOfDirectory(addonHandle)

def playhighlight(url):
  xbmc.log("XBMC trying to play URL [%s]" % (url), xbmc.LOGNOTICE)
  mediaAuth = utils.salt()
  if utils.head(url,dict(mediaAuth=mediaAuth)):
    completeUrl = url + ("|Cookie=mediaAuth%%3D%%22%s%%22" % (mediaAuth))
    xbmc.Player().play(completeUrl)


def playgame(date,feedId,provider):
  def adjustQuality(masterUrl):
    _720p60fps = "720p 60fps"
    qualityUrlDict = {
      "360p": "1200K/1200_complete.m3u8",
      "540p": "2500K/2500_complete.m3u8",
      "720p": "3500K/3500_complete.m3u8",
    }
    current = addon.getSetting("quality")
    if current is None or current == _720p60fps or current == "": 
      return masterUrl
    else:
      m3u8Path = qualityUrlDict.get(current, "3500K/3500_complete.m3u8")
      xbmc.log("Quality adjusted to '{0}', adjusting to {1}.".format(current, m3u8Path), xbmc.LOGNOTICE)
      return masterUrl.rsplit('/',1)[0] + "/" + m3u8Path

  def xbmcPlayer(url,mediaAuth):
    xbmc.log("XBMC trying to play URL [%s]" % (url), xbmc.LOGNOTICE)
    completeUrl = url + ("|Cookie=mediaAuth%%3D%%22%s%%22" % (mediaAuth))
    xbmc.Player().play(adjustQuality(url) + ("|Cookie=mediaAuth%%3D%%22%s%%22" % (mediaAuth)))
    #player.LazyManPlayer().play(adjustQuality(url) + ("|Cookie=mediaAuth%%3D%%22%s%%22" % (mediaAuth)))

  cdn = 'akc' if addon.getSetting("cdn") == "Akamai" else 'l3c'

  def getContentUrl(withCdn = True):
    actualCdn = cdn if withCdn else ""
    if provider == "NHL.tv":
      return "http://freegamez.ga/m3u8/%s/%s%s" % (date,feedId,actualCdn)
    else:
      return "http://freegamez.ga/mlb/m3u8/%s/%s%s" % (date,feedId,actualCdn)

  contentUrl = getContentUrl()
  xbmc.log("Trying to resolve from content-url: '" + contentUrl  + "'", xbmc.LOGNOTICE)
  if not utils.head(contentUrl):
    contentUrl = getContentUrl(False)
    if not utils.head(contentUrl):
      xbmc.log("Cannot resolve content-url '" + contentUrl + "'", xbmc.LOGERROR)
      raise ValueError("Invalid content-url '" + contentUrl + "'") 
  response = urllib.urlopen(contentUrl)
  playUrl = response.read().replace('l3c',cdn)
  xbmc.log("Play URL resolved to : '" + playUrl  + "'", xbmc.LOGNOTICE)
  mediaAuthSalt = utils.salt()
  if utils.head(playUrl,dict(mediaAuth=mediaAuthSalt)):
    xbmcPlayer(playUrl,mediaAuthSalt)
  else:
    otherCdn = 'akc' if cdn == 'l3c' else 'l3c' 
    xbmc.log("URL [%s] failed on HEAD, switching CDN from %s to %s" % (playUrl,cdn,otherCdn), xbmc.LOGNOTICE)
    xbmcPlayer(playUrl.replace(cdn,otherCdn), mediaAuthSalt)

def router(paramstring):
  params = dict(parse_qsl(paramstring))
  if params:
    if params['action'] == 'feeds':
      dategames = games(params['date'],params['provider'])
      gameDict = dict(map(lambda g: (g.id, g), dategames))
      listfeeds(gameDict[int(params['game'])], params['date'],params['provider'])
    elif params['action'] == 'play':
      playgame(params['date'],params['feedId'],params['provider'])
    elif params['action'] == 'listyears':
      listyears(params['provider'])
    elif params['action'] == 'listhighlights':
      listhighlights(params['provider'])
    elif params['action'] == 'listgrouphighlights':
      listgrouphighlights(params['provider'],params['group'])
    elif params['action'] == 'playhighlight':
      playhighlight(params['url'])
    elif params['action'] == 'listmonths':
      listmonths(params['year'],params['provider'])
    elif params['action'] == 'listdays':
      listdays(params['year'],params['month'],params['provider'])
    elif params['action'] == 'listgames':
      listgames("%d-%02d-%02d" % (int(params['year']),int(params['month']),int(params['day'])),params['provider'])
    elif params['action'] == 'listtodaysgames':
      listgames(utils.today().strftime("%Y-%m-%d"),params['provider'],True,True)
  else:
    listproviders()

def sanityCheck():
  since = addon.getSetting("sanityChecked")
  if since == "" or calendar.timegm(time.gmtime()) - (3600*24) > long(since):
    providers = config.get("LazyMan","Providers").split(",")
    for service in providers:
      xbmc.executebuiltin("Notification(LazyMan,Verifying " + service + ")")
      hostName = config.get(service,"Host")
      lazymanServer = config.get(service,"LazyManIP")
      resolved = socket.gethostbyname(hostName)
      if resolved != lazymanServer:
        xbmcgui.Dialog().ok(addonName, "'" + hostName + "' doesn't resolve to the Lazyman server.", "Update your hosts file to point to " + lazymanServer)
        xbmc.executebuiltin("XBMC.Container.Update(path,replace)")
      else:
        addon.setSetting("sanityChecked",str(calendar.timegm(time.gmtime())))

if __name__ == '__main__':
  sanityCheck()
  router(sys.argv[2][1:])

