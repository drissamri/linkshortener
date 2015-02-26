package be.drissamri.service;

import be.drissamri.entity.LinkEntity;

import java.util.List;

public interface LinkService {

  List<LinkEntity> find(int offset, int limit);

  LinkEntity create(String url);

  String findUrlByHash(String hash);

  void deleteByHash(String hash);

}
