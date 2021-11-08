package com.openclassrooms.realestatemanager;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.realestatemanager.database.EstateRoomDatabase;
import com.openclassrooms.realestatemanager.provider.EstateContentProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ContentProviderTest {

    private ContentResolver mContentResolver;

    public static long ESTATE_ID = 1;

    @Before
    public void setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), EstateRoomDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = InstrumentationRegistry.getContext().getContentResolver();
    }

    @Test
    public void getEstateWhenNoEstateInserted() {
        final Cursor cursor = mContentResolver.query(
                ContentUris.withAppendedId(EstateContentProvider.Companion.getURI_MENU(), ESTATE_ID),
                null,
                null,
                null,
                null);
        Assert.assertThat(cursor, notNullValue());
        Assert.assertThat(cursor.getCount(), is(0));
        cursor.close();
    }
}
