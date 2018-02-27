import urllib, json
import xbmc
from ConfigParser import NoOptionError

def get_highlights(config,provider): 
  data = None
  try:
    response = urllib.urlopen(config.get(provider,"HighlightsUrl"))
    data = json.loads(response.read())
  except NoOptionError:
    return []
  highlights = []
  for t in data['topics']:
    title = t['title']
    title_highlights = []
    for h in t['list']:
      blurb = h['blurb']
      duration = h['duration']
      playbacks = filter(lambda x: x['name'] == 'HTTP_CLOUD_WIRED_60', h['playbacks'])
      if (len(playbacks) > 0):
        xbmc.log("Creating highlight [%s (%s)] from %s" % (blurb,duration,playbacks[0]['url']), xbmc.LOGNOTICE)
        title_highlights.append(Highlight(blurb,duration,playbacks[0]['url']))
    highlights.append(HighlightGroup(title,title_highlights))
  return highlights


class HighlightGroup(object):
  highlights = []
  title = None
  def __init__(self,title_,highlights_):
    self.title = title_
    self.highlights = highlights_

class Highlight(object):
  playbackUrl = None
  duration = None
  blurb = None
  def __init__(self,blurb_,duration_,playback_):
    self.blurb = blurb_
    self.duration = duration_
    self.playbackUrl = playback_

  def viewable(self): return True
