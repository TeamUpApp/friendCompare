package com.example.clazell.bestfriends;

/**
 * Created by nrobatmeily on 30/10/2014.
 */
public class Word implements Comparable<Word>
{
    String word;
    int count;

    @Override
    public int hashCode()
    {
        return word.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        return word.equals(((Word)obj).word);
    }

    @Override
    public int compareTo(Word b)
    {
        return b.count - count;
    }
}