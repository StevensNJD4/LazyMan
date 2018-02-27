from datetime import datetime, timedelta
from pytz import timezone
from pytz import reference
import calendar
import time
import pytz
import random
import requests

losangeles = timezone('America/Los_Angeles')
localtz = reference.LocalTimezone()

def today(tz = losangeles):
  date = datetime.now()
  return tz.localize(date)

def asCurrentTz(d,t): 
  parsed = None
  try:
    parsed = datetime.strptime(d + " " + t,'%Y-%m-%d %H:%M:%S')
  except TypeError:
    parsed = datetime(*(time.strptime(d + " " + t, '%Y-%m-%d %H:%M:%S')[0:6]))
  replaced = parsed.replace(tzinfo=timezone('UTC'))
  local = replaced.astimezone(localtz)
  return "%02d:%02d" % (local.hour, local.minute)


def years(provider): 
  start = 2017 if provider == "MLB.tv" else 2015
  return xrange(start,today().year + 1)

def months(year): 
  if int(year) == today().year:
    return map(lambda m: (calendar.month_name[m],m), xrange(1,today().month + 1))
  else:
    return map(lambda m: (calendar.month_name[m],m), xrange(1,13))

def days(year,month): 
  if int(year) == today().year and int(month) == today().month:
    return xrange(1,today().day)
  else:
    r = calendar.monthrange(int(year),int(month))
    return xrange(1,max(r)+1)

def garble(salt = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"): return ''.join(random.sample(salt,len(salt)))

def salt():
  garbled = garble()
  return ''.join([garbled[int(i * random.random()) % len(garbled)]  for i in range(0,241)])

def head(url,cookies = dict()):
  print "Checking url %s" % (url)
  return requests.request('HEAD',url,cookies = cookies).status_code < 400
