import http.client
import os
import unittest
from urllib.request import urlopen

import pytest

BASE_URL = os.environ.get("BASE_URL")
DEFAULT_TIMEOUT = 2  # in secs


@pytest.mark.api
class TestApi(unittest.TestCase):
    def setUp(self):
        self.assertIsNotNone(BASE_URL, "URL no configurada")
        self.assertTrue(len(BASE_URL) > 8, "URL no configurada")

    def test_api_add(self):
        url = f"{BASE_URL}/calc/add/2/2"
        response = urlopen(url, timeout=DEFAULT_TIMEOUT)
        self.assertEqual(response.read().decode(), "4")
        
    def test_api_substract(self):
        url = f"{BASE_URL}/calc/substract/2/2"
        response = urlopen(url, timeout=DEFAULT_TIMEOUT)
        self.assertEqual(response.read().decode(), "0")
        
    def test_api_multiply(self):
        url = f"{BASE_URL}/calc/multiply/2/3"
        response = urlopen(url, timeout=DEFAULT_TIMEOUT)
        self.assertEqual(response.read().decode(), "6")
        
    def test_api_divide(self):
        url = f"{BASE_URL}/calc/divide/2/2"
        response = urlopen(url, timeout=DEFAULT_TIMEOUT)
        self.assertEqual(response.read().decode(), "1.0")
        
    def test_api_power(self):
        url = f"{BASE_URL}/calc/power/2/3"
        response = urlopen(url, timeout=DEFAULT_TIMEOUT)
        self.assertEqual(response.read().decode(), "8")
        
    def test_api_sqrt(self):
        url = f"{BASE_URL}/calc/sqrt/9"
        response = urlopen(url, timeout=DEFAULT_TIMEOUT)
        self.assertEqual(response.read().decode(), "3.0")
        
    def test_api_log(self):
        url = f"{BASE_URL}/calc/log/100"
        response = urlopen(url, timeout=DEFAULT_TIMEOUT)
        self.assertEqual(response.read().decode(), "2.0")
        
    def test_add_with_nan_parameter(self):
        url = f"{BASE_URL}/calc/add/2/a"
        with self.assertRaises(Exception) as cm:
            urlopen(url, timeout=DEFAULT_TIMEOUT)
        self.assertEqual(cm.exception.code, http.client.BAD_REQUEST)
        
    def test_substract_with_nan_parameter(self):
        url = f"{BASE_URL}/calc/substract/a/2"
        with self.assertRaises(Exception) as cm:
            urlopen(url, timeout=DEFAULT_TIMEOUT)
        self.assertEqual(cm.exception.code, http.client.BAD_REQUEST)
        
    def test_multiply_with_nan_parameter(self):
        url = f"{BASE_URL}/calc/multiply/2/None"
        with self.assertRaises(Exception) as cm:
            urlopen(url, timeout=DEFAULT_TIMEOUT)
        self.assertEqual(cm.exception.code, http.client.BAD_REQUEST)
    
    def test_api_divide_by_zero(self):
        url = f"{BASE_URL}/calc/divide/2/0"
        with self.assertRaises(Exception) as cm:
            urlopen(url, timeout=DEFAULT_TIMEOUT)
        self.assertEqual(cm.exception.code, http.client.BAD_REQUEST)
        
    def test_power_with_nan_parameter(self):
        url = f"{BASE_URL}/calc/add/b/a"
        with self.assertRaises(Exception) as cm:
            urlopen(url, timeout=DEFAULT_TIMEOUT)
        self.assertEqual(cm.exception.code, http.client.BAD_REQUEST)
        
    def test_api_sqrt_of_negative_number(self):
        url = f"{BASE_URL}/calc/sqrt/-1"
        with self.assertRaises(Exception) as cm:
            urlopen(url, timeout=DEFAULT_TIMEOUT)
        self.assertEqual(cm.exception.code, http.client.BAD_REQUEST)
            
    def test_api_log_of_negative_number(self):
        url = f"{BASE_URL}/calc/log/-1"
        with self.assertRaises(Exception) as cm:
            urlopen(url, timeout=DEFAULT_TIMEOUT)
        self.assertEqual(cm.exception.code, http.client.BAD_REQUEST)
            
    def test_api_log_of_negative_number(self):
        url = f"{BASE_URL}/calc/log/0"
        with self.assertRaises(Exception) as cm:
            urlopen(url, timeout=DEFAULT_TIMEOUT)
        self.assertEqual(cm.exception.code, http.client.BAD_REQUEST)
