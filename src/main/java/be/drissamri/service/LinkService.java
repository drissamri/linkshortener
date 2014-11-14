package be.drissamri.service;

import be.drissamri.entity.LinkEntity;

import java.util.List;

public interface LinkService {

  List<LinkEntity> find();

  LinkEntity create(String url);

  void deleteByHash(String hash);

}
