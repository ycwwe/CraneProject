package com.example.communication;

//package com.abb.cranes.xjd.aalp;

public enum ComLinkState
{
  WaitingOnClient,  WaitingOnServer,  Connected,  Disconnected;
  
  private ComLinkState() {}
}
