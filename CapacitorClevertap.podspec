require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name = 'CapacitorClevertap'
  s.version = package['version']
  s.summary = package['description']
  s.license = package['license']
  s.homepage = package['repository']['url']
  s.author = package['author']
  s.source = { :git => package['repository']['url'], :tag => s.version.to_s }
  s.source_files = 'ios/Sources/**/*.{swift,h,m,c,cc,mm,cpp}'
  s.ios.deployment_target = '14.0'
  s.dependency 'Capacitor'
  s.dependency 'CleverTap-iOS-SDK', '~> 7.8', '>= 7.8.2'
  s.dependency 'CleverTap-Geofence-SDK', '~> 1.0', '>= 1.0.7'
  s.dependency 'CTNotificationService', '~> 0.1', '>= 0.1.7'
  s.swift_version = '5.1'
end
