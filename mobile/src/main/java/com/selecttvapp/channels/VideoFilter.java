package com.selecttvapp.channels;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by babin on 7/7/2017.
 */


public class VideoFilter {

    public static int getCurentVideoPosition(ArrayList<ProgramList> mProgramList) {
        try {
            final long nowAsPerDeviceTimeZone = ChannelUtils.GetUnixTime();
            Predicate<ProgramList> getCurrentVideo = new Predicate<ProgramList>() {
                @Override
                public boolean apply(@javax.annotation.Nullable ProgramList list) {
                    return (ChannelUtils.getDurationFromDate(list.getStart_at()) <= nowAsPerDeviceTimeZone && ChannelUtils.getDurationFromDate(list.getEnd_at()) > nowAsPerDeviceTimeZone);
                }


            };
            return Iterables.indexOf(mProgramList, getCurrentVideo);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static ChannelScheduler getSelectedChannel(ArrayList<ChannelScheduler> mProgramList, final String slug) {
        Predicate<ChannelScheduler> getCurrentVideo = new Predicate<ChannelScheduler>() {
            @Override
            public boolean apply(@javax.annotation.Nullable ChannelScheduler list) {
                return (list.getSlug().equalsIgnoreCase(slug));
            }


        };
        Collection<ChannelScheduler> cs = Collections2.filter(mProgramList, getCurrentVideo);

        List<ChannelScheduler> filteredCopy = Lists.newArrayList(cs);
        if (filteredCopy.size() > 0)
            return filteredCopy.get(0);
        return null;
    }

    public static ArrayList<ProgramList> removePastVideos(ArrayList<ProgramList> mProgramList) {
        try {
            Predicate<ProgramList> getCurrentVideo = new Predicate<ProgramList>() {
                @Override
                public boolean apply(@javax.annotation.Nullable ProgramList list) {
                    try {
                        assert list != null;
                        return (ChannelUtils.getDurationFromDate(list.getEnd_at()) > ChannelUtils.GetUnixTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            };
            Collection<ProgramList> cs = Collections2.filter(mProgramList, getCurrentVideo);
            ArrayList<ProgramList> filteredCopy = Lists.newArrayList(cs);
            int pos = mProgramList.indexOf(filteredCopy.get(0));
            if (pos != 0) {
                filteredCopy.add(0, mProgramList.get(pos - 1));
            }
            ArrayList<ProgramList> realmFilteredCopy = new ArrayList<>();
            realmFilteredCopy.addAll(filteredCopy);
            return realmFilteredCopy;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<ProgramList>();
    }

    public static int getSelectedVideoPosition(ArrayList<ProgramList> mProgramList, final String uid) {
        try {
            Predicate<ProgramList> getCurrentVideo = new Predicate<ProgramList>() {
                @Override
                public boolean apply(@javax.annotation.Nullable ProgramList list) {
                    return uid.equalsIgnoreCase(list.getUuid());
                }


            };
            return Iterables.indexOf(mProgramList, getCurrentVideo);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int getSelectedChannelPosition(ArrayList<ChannelScheduler> mProgramList, final String slug) {
        Predicate<ChannelScheduler> getCurrentVideo = new Predicate<ChannelScheduler>() {
            @Override
            public boolean apply(@javax.annotation.Nullable ChannelScheduler list) {
                return (list.getSlug().equalsIgnoreCase(slug));
            }


        };
        return Iterables.indexOf(mProgramList, getCurrentVideo);
    }

}
